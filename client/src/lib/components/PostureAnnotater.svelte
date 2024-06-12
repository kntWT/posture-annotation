<script lang="ts">
	import type { Posture, PostureUpdate } from "$api/generated";
	import { isLoggedIn, getUser } from "$lib/store/user";
	import type { Image } from "p5";
	import type p5 from "p5";
    import P5 from "p5-svelte";

    export let handleAction: (data: PostureUpdate) => Promise<unknown>;
    export let posture: Posture;
    export let imageSrc: string;

    let correctedNeckAngle: number = posture.neckAngle;
    let correctedTorsoAngle: number = posture.torsoAngle;
    let scale: number = 1;
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
        const data: PostureUpdate = {
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

    const sketch = (p: p5) => {
        let img: Image;
        const tragus = p.createVector(0, 0);
        const shoulder = p.createVector(0, 0);
        const waist = p.createVector(0, 0);
        const radius = 10;
        let target: p5.Vector | null = null;
        const imageOffset = p.createVector(0, 0);
        let pMouse: p5.Vector | null = null;

        p.preload = () => {
            img = p.loadImage(imageSrc);
        }

        p.setup = () => {
            const container = document.getElementById("annotater-container");
            p.createCanvas(container?.clientWidth ?? 400, container?.clientHeight ?? 600);
            p.imageMode(p.CENTER);
            const len = p.height / 6;
            waist.set(p.width / 2, p.height * 2 / 3);
            shoulder.set(waist.x - len*p.sin(p.radians(correctedTorsoAngle)), waist.y - len*p.cos(p.radians(correctedTorsoAngle)));
            tragus.set(shoulder.x - len*p.sin(p.radians(correctedNeckAngle)), shoulder.y - len*p.cos(p.radians(correctedNeckAngle)));
        }

        p.draw = () => {
            p.background(255);
            const aspectRatio = img.width / img.height;
            p.image(img, p.width/2 + (imageOffset.x*scale), p.height/2 + (imageOffset.y*scale), aspectRatio*p.height * scale , p.height * scale);

            p.stroke(255, 255, 0);
            p.strokeWeight(5);
            p.line(waist.x, waist.y, shoulder.x, shoulder.y);
            p.line(shoulder.x, shoulder.y, tragus.x, tragus.y);

            p.fill(255, 0, 0);
            p.ellipse(tragus.x, tragus.y, radius*2, radius*2);
            p.fill(0, 255, 0);
            p.ellipse(shoulder.x, shoulder.y, radius*2, radius*2);
            p.fill(0, 0, 255);
            p.ellipse(waist.x, waist.y, radius*2, radius*2);

            if(p.mouseIsPressed) {
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
            } else {
                target = null;
                pMouse = null;
            }
        }

        const distToMouse = (point: p5.Vector): number => {
            return p.dist(point.x, point.y, p.mouseX, p.mouseY);
        }

        const setTarget = () => {
            if (distToMouse(tragus) < radius) {
                target = tragus;
            }
            else if (distToMouse(shoulder) < radius) {
                target = shoulder;
            }
            else if (distToMouse(waist) < radius) {
                target = waist;
            }
        }

        const updateMarkPosition = () => {
            if (target === null) return;

            if (distToMouse(target) < radius) {
                target.set(p.mouseX, p.mouseY);
                correctedNeckAngle = calcAngle(shoulder, tragus);
                return;
            }
        }

        const updateImageOffset = () => {
            if (pMouse === null) return;

            imageOffset.add(p.mouseX - pMouse.x, p.mouseY - pMouse.y);
        }

        const calcAngle = (p1: p5.Vector, p2: p5.Vector): number => {
            const theta = p.acos(((p2.y-p1.y) * -p1.y )/ (p.dist(p1.x, p1.y, p2.x, p2.y) * p1.y));
            return p.degrees(theta);
        }
    }

</script>

<div id="annotater-container">
    <div>
        <h2>#{posture.id}</h2>
        <p>首の角度: {correctedNeckAngle.toFixed(2)}</p>
        <p>胴体の角度: {correctedTorsoAngle.toFixed(2)}</p>
        <button on:click={handleSubmit}>保存</button>
        <div>
            <button on:click={decrementScale}>-</button>
            <input type="number" step="0.05" value={scale.toFixed(2)} on:change={handleInputChange} />
            <button on:click={incrementScale}>+</button>
        </div>
    </div>
    <P5 {sketch} />

</div>

<style lang="scss">
    #annotater-container {
        text-align: center;
        width: 100vw;
        height: 45vh;
        
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