<script lang="ts">
	import type { Posture, PostureUpdateWithFile } from "$api/generated";
	import { isLoggedIn, getUser } from "$lib/store/user";
    import { onMount, onDestroy } from "svelte";
    import { browser } from '$app/environment';
	import type p5 from "p5";
    import P5 from "p5-svelte";

    export let handleAction: (data: PostureUpdateWithFile) => Promise<unknown>;
    export let posture: Posture;
    export let imageSrc: string;
    export let showWaist: boolean = false;
    export let holdShoulder: boolean = false;

    let correctedNeckAngle: number = posture.neckAngle;
    let correctedTorsoAngle: number = posture.torsoAngle;
    let scale: number = 1;
    const step = 0.05;

    onMount(() => {
        document.addEventListener("keydown", handleKeyDown);
    });

    onDestroy(() => {
        if (browser) {
            document.removeEventListener("keydown", handleKeyDown);
        }
    });

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

    // Enterキーでsubmitできるようにする
    const handleKeyDown = (e: KeyboardEvent) => {
        if (e.key === "Enter") {
            handleSubmit();
        }
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
        const marginRadius = 5;
        const strokeWeight = 2;
        const aspectRatio = posture.imageWidth / posture.imageHeight;
        const alpha = 160;
        let target: p5.Vector | null = null;
        let lastTarget: p5.Vector | null = null;
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
            imageOffset.set(0, 0);
            const len = p.height / 6;
            if (posture.waistX && posture.waistY) {
                waist.set(...adjustMarkerPosition(posture.waistX, posture.waistY));
            } else {
                waist.set(p.width / 2, p.height * 2 / 3);
            }
            if (posture.shoulderX && posture.shoulderY) {
                shoulder.set(...adjustMarkerPosition(posture.shoulderX, posture.shoulderY));
            } else {
                shoulder.set(
                    waist.x - len*p.sin(p.radians(correctedTorsoAngle)),
                    waist.y - len*p.cos(p.radians(correctedTorsoAngle))
                );
            }
            if (posture.tragusX && posture.tragusY) {
                tragus.set(...adjustMarkerPosition(posture.tragusX, posture.tragusY));
            } else {
                tragus.set(
                    shoulder.x - len*p.sin(p.radians(correctedNeckAngle)),
                    shoulder.y - len*p.cos(p.radians(correctedNeckAngle))
                );
            }
            adjustFrame([tragus, shoulder, waist]);
            if (holdShoulder) {
                target = shoulder;
            }
        }

        p.draw = () => {
            p.background(255);
            p.push();
                p.translate(p.width/2 + imageOffset.x, p.height/2 + imageOffset.y);
                p.scale(scale);
                p.image(img, 0, 0, aspectRatio*p.height, p.height);
                drawMarks();
            p.pop();

            if(target !== null || pMouse !== null) {
                mosueMoved();
            }
        }

        p.mousePressed = () => {
            mousePressed();
        }

        p.mouseReleased = () => {
            mouseReseased();
        }

        const drawMarks = () => {
            const correctedWaist = p.createVector(
                waist.x - p.width/2,
                waist.y - p.height/2
            );
            const correctedShoulder = p.createVector(
                shoulder.x - p.width/2,
                shoulder.y - p.height/2
            );
            const correctedTragus = p.createVector(
                tragus.x - p.width/2,
                tragus.y - p.height/2
            );
            p.stroke(255, 255, 0);
            p.strokeWeight(strokeWeight/scale);
            p.line(correctedShoulder.x, correctedShoulder.y, correctedTragus.x, correctedTragus.y);
            if (showWaist) {
                p.line(correctedWaist.x, correctedWaist.y, correctedShoulder.x, correctedShoulder.y);
            }

            p.noStroke();
            p.fill(255, 0, 0, alpha);
            p.ellipse(correctedTragus.x, correctedTragus.y, radius*2/scale, radius*2/scale);
            p.fill(0, 255, 0, alpha);
            p.ellipse(correctedShoulder.x, correctedShoulder.y, radius*2/scale, radius*2/scale);
            if (showWaist) {
                p.fill(0, 0, 255, alpha);
                p.ellipse(correctedWaist.x, correctedWaist.y, radius*2/scale, radius*2/scale);
            }
        }

        const mousePressed = () => {
            if (!mouseInCanvas()) return;

            const mouse = p.createVector(
                (p.mouseX - imageOffset.x - p.width/2)/scale + p.width/2,
                (p.mouseY - imageOffset.y - p.height/2)/scale + p.height/2
            );
            if (target === null) {
                setTarget(mouse);
                if (target === null && pMouse === null) {
                    pMouse = p.createVector(p.mouseX, p.mouseY);
                }
            }
        }

        const mosueMoved = () => {
            if (target === null && pMouse === null) return;

            const mouse = p.createVector(
                (p.mouseX - imageOffset.x - p.width/2)/scale + p.width/2,
                (p.mouseY - imageOffset.y - p.height/2)/scale + p.height/2
            );
            updateMarkPosition(mouse);
            updateImageOffset();
            if (pMouse !== null) {
                pMouse.set(p.mouseX, p.mouseY);
            }
        }

        const mouseReseased = () => {
            if (!mouseInCanvas()) return;

            lastTarget = target;
            target = null;
            pMouse = null;
        }

        p.keyPressed = () => {
            const _step = 1 / scale;
            switch (p.keyCode) {
                case p.ESCAPE:
                    resetTarget();
                    break;
                case p.UP_ARROW:
                    if (lastTarget) {
                        lastTarget.add(0, -_step);
                    }
                    break;
                case p.DOWN_ARROW:
                    if (lastTarget) {
                        lastTarget.add(0, _step);
                    }
                    break;
                case p.LEFT_ARROW:
                    if (lastTarget) {
                        lastTarget.add(-_step, 0);
                    }
                    break;
                case p.RIGHT_ARROW:
                    if (lastTarget) {
                        lastTarget.add(_step, 0);
                    }
                    break;
            }
        }

        const resetTarget = () => {
            const _target = target ?? lastTarget;
            if (_target == tragus) {
                if (posture.tragusX && posture.tragusY) {
                    tragus.set(...adjustMarkerPosition(posture.tragusX, posture.tragusY));
                } else {
                    tragus.set(
                        shoulder.x - p.height/6*p.sin(p.radians(correctedNeckAngle)),
                        shoulder.y - p.height/6*p.cos(p.radians(correctedNeckAngle))
                    );
                }
            } else if (_target == shoulder) {
                if (posture.shoulderX && posture.shoulderY) {
                    shoulder.set(...adjustMarkerPosition(posture.shoulderX, posture.shoulderY));
                } else {
                    shoulder.set(
                        waist.x - p.height/6*p.sin(p.radians(correctedTorsoAngle)),
                        waist.y - p.height/6*p.cos(p.radians(correctedTorsoAngle))
                    );
                }
            } else if (_target == waist) {
                if (posture.waistX && posture.waistY) {
                    waist.set(...adjustMarkerPosition(posture.waistX, posture.waistY));
                } else {
                    waist.set(p.width / 2, p.height * 2 / 3);
                }
            }
            target = null;
        }

        const mouseInCanvas = (): boolean => {
            return (
                p.mouseX >= 0 && p.mouseX <= p.width
                && p.mouseY >= 0 && p.mouseY <= p.height
            );
        }

        const distToMouse = (point: p5.Vector, mouse: p.Vector = p.createVector(p.mouseX, p.mouseY)): number => {
            return p.dist(point.x, point.y, mouse.x, mouse.y);
        }

        const setTarget = (mouse: p.Vector = p.createVector(p.mouseX, p.mouseY)) => {
            const r = radius + marginRadius;
            if (distToMouse(tragus, mouse) < r) {
                target = tragus;
            } else if (distToMouse(shoulder, mouse) < r) {
                target = shoulder;
            } else if (distToMouse(waist, mouse) < r) {
                target = waist;
            }
        }

        const updateMarkPosition = (mouse: p.Vector = p.createVector(p.mouseX, p.mouseY)) => {
            if (target === null) return;

            target.add(mouse.x - target.x, mouse.y - target.y);
            correctedNeckAngle = calcAngle(shoulder, tragus);
        }

        const calcAngle = (p1: p5.Vector, p2: p5.Vector): number => {
            const theta = p.acos(((p2.y-p1.y) * -p1.y )/ (p.dist(p1.x, p1.y, p2.x, p2.y) * p1.y));
            return p.degrees(theta);
        }

        const updateImageOffset = (mouse: p.Vector = p.createVector(p.mouseX, p.mouseY)) => {
            if (pMouse === null) return;

            imageOffset.add(mouse.x - pMouse.x, mouse.y - pMouse.y);
        }

        const adjustMarkerPosition = (x: number, y: number): number[] => {
            const rate = p.height / posture.imageHeight;
            const imageLeft = p.width/2 - (posture.imageWidth/2)*rate;
            const imageTop = p.height/2 - (posture.imageHeight/2)*rate;
            return [
                imageLeft + x*rate,
                imageTop + y*rate
            ]
        }

        const adjustFrame = (points: p.Vector[], margin: number = 0.5) => {
            const left = Math.min(...points.map(p => p.x));
            const right = Math.max(...points.map(p => p.x));
            const top = Math.min(...points.map(p => p.y));
            const bottom = Math.max(...points.map(p => p.y));
            const w = right - left;
            const h = bottom - top;
            const g = p.createVector(
                points.reduce((acc, p) => acc + p.x, 0) / points.length,
                points.reduce((acc, p) => acc + p.y, 0) / points.length
            );
            const rate = Math.min(p.height*aspectRatio / (w * (1+margin)), p.height / (h * (1+margin)));
            scale = rate;
            imageOffset.set(
                -(g.x - p.width/2) * rate,
                -(g.y - p.height/2) * rate
            );
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