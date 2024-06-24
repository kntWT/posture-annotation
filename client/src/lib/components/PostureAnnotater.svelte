<script lang="ts">
	import type { Posture, PostureUpdateWithFile } from "$api/generated";
	import { isLoggedIn, getUser } from "$lib/store/user";
	import type p5 from "p5";
    import P5 from "p5-svelte";

    export let handleAction: (data: PostureUpdateWithFile) => Promise<unknown>;
    export let posture: Posture;
    export let imageSrc: string;
    export let showWaist: boolean = false;

    let correctedNeckAngle: number = posture.neckAngle;
    let correctedTorsoAngle: number = posture.torsoAngle;
    let scale: number = 2;
    const step = 0.05;

    // FIXME: なぜか型アノテーションが変なのでanyで回避
    const handleInputChange = (e: any) => {
        const target = e.target as HTMLInputElement;
        scale = parseFloat(target.value);
    }

    const handleSubmit = async () => {
        const user = getUser();
        if (!isLoggedIn() || !user) {
            alert("ログインしてください");
            return;
        }
        const canvas = document.querySelector("canvas") as HTMLCanvasElement;
        const img = canvasToBase64(canvas);
        if (!img || img === "") {
            alert("キャンバスの読み込みに失敗しました");
            return;
        }
        const data: PostureUpdateWithFile = {
            file: img,
            neckAngle: correctedNeckAngle,
            torsoAngle: correctedTorsoAngle,
            annotaterId: user.id,
        };
        await handleAction(data);
    }

    const incrementScale = () => {
        scale += step;
    }

    const decrementScale = () => {
        scale -= step;
    }

    const canvasToBase64 = (canvas: HTMLCanvasElement): string => {
            return canvas.toDataURL("image/jpeg");
    }

    const sketch = (p: p5) => {
        let img: p5.Image;
        const tragus = p.createVector(0, 0);
        const shoulder = p.createVector(0, 0);
        const waist = p.createVector(0, 0);
        const radius = 4;
        const marginRadius = 15;
        const strokeWeight = 2;
        const alpha = 160;
        let target: p5.Vector | null = null;
        const imageOffset = p.createVector(0, 0);
        let pMouse: p5.Vector | null = null;

        p.preload = () => {
            img = p.loadImage(imageSrc);
        }

        p.setup = () => {
            const container = document.getElementById("annotater-container");
            const height = container?.clientHeight ?? 600;
            const width = Math.min(container?.clientWidth ?? 400, height * 16 / 9);
            p.createCanvas(width, height);
            p.imageMode(p.CENTER);
            imageOffset.set(0, -height*0.1);
            const len = p.height / 6;
            waist.set(p.width / 2, p.height * 2 / 3);
            shoulder.set(waist.x - len*p.sin(p.radians(correctedTorsoAngle)), waist.y - len*p.cos(p.radians(correctedTorsoAngle)));
            tragus.set(shoulder.x - len*p.sin(p.radians(correctedNeckAngle)), shoulder.y - len*p.cos(p.radians(correctedNeckAngle)));
        }

        p.draw = () => {
            drawImage();
            drawMarks();

            if(p.mouseIsPressed) {
                mousePressed();
            } else {
                mouseReseased();
            }
        }

        const drawImage = () => {
            const aspectRatio = img.width / img.height;
            p.image(
                img,
                p.width/2 + (imageOffset.x*scale),
                p.height/2 + (imageOffset.y*scale),
                aspectRatio*p.height * scale,
                p.height * scale
            );
        }

        const drawMarks = () => {
            p.stroke(255, 255, 0);
            p.strokeWeight(strokeWeight);
            p.line(shoulder.x, shoulder.y, tragus.x, tragus.y);
            if (showWaist) {
                p.line(waist.x, waist.y, shoulder.x, shoulder.y);
            }

            p.noStroke();
            p.fill(255, 0, 0, alpha);
            p.ellipse(tragus.x, tragus.y, radius*2, radius*2);
            p.fill(0, 255, 0, alpha);
            p.ellipse(shoulder.x, shoulder.y, radius*2, radius*2);
            if (showWaist) {
                p.fill(0, 0, 255, alpha);
                p.ellipse(waist.x, waist.y, radius*2, radius*2);
            }
        }

        const mousePressed = () => {
            if (target === null) {
                setTarget();
                if (target === null && pMouse === null) {
                    pMouse = p.createVector(p.mouseX, p.mouseY);
                }
            } else {
                updateMarkPosition();
            }
            updateImageOffset();
            if (pMouse !== null) {
                pMouse.set(p.mouseX, p.mouseY);
            }
        }

        const mouseReseased = () => {
            target = null;
            pMouse = null;
        }

        const distToMouse = (point: p5.Vector): number => {
            return p.dist(point.x, point.y, p.mouseX, p.mouseY);
        }

        const setTarget = () => {
            if (distToMouse(tragus) < radius + marginRadius) {
                target = tragus;
            }
            else if (distToMouse(shoulder) < radius + marginRadius) {
                target = shoulder;
            }
            else if (distToMouse(waist) < radius + marginRadius) {
                target = waist;
            }
        }

        const updateMarkPosition = () => {
            if (target === null) return;

            if (distToMouse(target) < radius + marginRadius) {
                target.set(p.mouseX, p.mouseY);
                correctedNeckAngle = calcAngle(shoulder, tragus);
                return;
            }
        }

        const calcAngle = (p1: p5.Vector, p2: p5.Vector): number => {
            const theta = p.acos(((p2.y-p1.y) * -p1.y )/ (p.dist(p1.x, p1.y, p2.x, p2.y) * p1.y));
            return p.degrees(theta);
        }

        const updateImageOffset = () => {
            if (pMouse === null) return;

            imageOffset.add(p.mouseX - pMouse.x, p.mouseY - pMouse.y);
        }
    }

</script>

<div id="annotater-container">
    <div>
        <h2>#{posture.id}</h2>
        <p>首の角度: {correctedNeckAngle.toFixed(2)}</p>
        {#if showWaist}
            <p>胴体の角度: {correctedTorsoAngle.toFixed(2)}</p>
        {/if}
        <button on:click={handleSubmit}>保存</button>
        <div>
            <button on:click={decrementScale}>-</button>
            <input type="number" step={step} value={scale.toFixed(2)} on:change={handleInputChange} />
            <button on:click={incrementScale}>+</button>
        </div>
    </div>
    <P5 {sketch} />

</div>

<style lang="scss">
    #annotater-container {
        text-align: center;
        width: 100vw;
        height: 50vh;
        
        div {
            text-align: center;
            margin: 8px;
        }
    }
    input {
        width: 20%;
        max-width: 60px;
        text-align: right;
    }
</style>