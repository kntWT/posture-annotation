<script lang="ts">
	import type { AnnotationCreateWithFile, Posture } from '$api/generated';
	import { isLoggedIn, getUser } from '$lib/store/user';
	import { onMount, onDestroy } from 'svelte';
	import { browser } from '$app/environment';
	import type p5 from 'p5';
	import P5 from 'p5-svelte';
	// import Button, { Label } from "@smui/button";
	import IconButton from '@smui/icon-button';
	import TextField from '@smui/textfield';

	type Vector = { x: number; y: number };

	export let handleAction: (data: AnnotationCreateWithFile) => Promise<unknown>;
	export let posture: Posture;
	export let imageSrc: string;
	export let showWaist: boolean = false;
	export let holdShoulder: boolean = false;

	const tragus: Vector = { x: posture.tragusX ?? 0, y: posture.tragusY ?? 0 };
	const shoulder: Vector = { x: posture.shoulderX ?? 0, y: posture.shoulderY ?? 0 };
	const waist: Vector = { x: posture.waistX ?? 0, y: posture.waistY ?? 0 };
	let correctedNeckAngle: number = posture.neckAngle;
	let correctedTorsoAngle: number = posture.torsoAngle;
	const imageWidth = posture.imageWidth ?? 1536;
	const imageHeight = posture.imageHeight ?? 2048;
	let scale: number = 1;
	const step = 0.05;
	let submitted: boolean = false;
	let canvas: HTMLCanvasElement | null = null;
	let canSubmit: boolean = false;
	let isMobile: boolean = false;

	onMount(() => {
		document.addEventListener('keydown', handleKeyDown);
		setTimeout(() => {
			canvas = document.querySelector('canvas');
			document.addEventListener('mousemove', handleUpdateCanSubmit);
			document.addEventListener('touchmove', handleUpdateCanSubmit);
		}, 1000);
		isMobile = navigator.userAgent.indexOf('Mobile') >= 0;
	});

	onDestroy(() => {
		if (browser) {
			document.removeEventListener('keydown', handleKeyDown);
			document.removeEventListener('mousemove', handleUpdateCanSubmit);
			document.removeEventListener('touchmove', handleUpdateCanSubmit);
		}
	});

	const handleUpdateCanSubmit = (e: MouseEvent | TouchEvent) => {
		const flag = canvas === e.target;
		if (e instanceof MouseEvent) {
			updateCanSubmit(e.clientX, e.clientY, flag);
		} else if (e instanceof TouchEvent) {
			updateCanSubmit(e.touches[0].clientX, e.touches[0].clientY, flag);
		}
	};

	const updateCanSubmit = (x: number, y: number, flag: boolean) => {
		if (!canvas) return;
		canSubmit =
			flag &&
			x >= canvas.getBoundingClientRect().left &&
			x <= canvas.getBoundingClientRect().right &&
			y >= canvas.getBoundingClientRect().top &&
			y <= canvas.getBoundingClientRect().bottom;
	};

	// FIXME: なぜか型アノテーションが変なのでanyで回避
	const handleInputChange = (e: any) => {
		const target = e.target as HTMLInputElement;
		scale = parseFloat(target.value);
	};

	const handleSubmit = async () => {
		// FIXME: なぜか過去のp5sketchが残っている（マウス座標などが再レンダリングされる直前のもののままmousePressedの送信処理が実行されてしまう）ので，フラグで管理
		if (submitted || !canSubmit) return;

		const user = getUser();
		if (!isLoggedIn() || !user) {
			alert('ログインしてください');
			return;
		}
		const canvas = document.querySelector('canvas') as HTMLCanvasElement;
		const img = canvasToBase64(canvas);
		if (!img || img === '') {
			alert('キャンバスの読み込みに失敗しました');
			return;
		}
		const fileName =
			imageSrc.split('/').pop() ??
			`${posture.exCreatedAt.toLocaleString().replace(' ', '_')}.${posture.exCreatedAt.getMilliseconds()}.jpg;`;

		const data: AnnotationCreateWithFile = {
			file: img,
			userId: posture.userId,
			fileName: fileName,
			tragusX: tragus.x,
			tragusY: tragus.y,
			shoulderX: shoulder.x,
			shoulderY: shoulder.y,
			waistX: waist.x,
			waistY: waist.y,
			neckAngle: correctedNeckAngle,
			torsoAngle: correctedTorsoAngle,
			annotaterId: user.id,
			postureId: posture.id
		};
		await handleAction(data);
		submitted = true;
	};

	// Enterキーまたはスペースでsubmitできるようにする
	const handleKeyDown = (e: KeyboardEvent) => {
		if (e.target instanceof HTMLInputElement) return;

		if (e.key === 'Enter' || e.key === ' ') {
			e.preventDefault();
			handleSubmit();
		}
	};

	const incrementScale = () => {
		scale += step;
	};

	const decrementScale = () => {
		scale -= step;
	};

	const canvasToBase64 = (canvas: HTMLCanvasElement): string => {
		return canvas.toDataURL('image/jpeg');
	};

	const sketch = (p: p5) => {
		let img: p5.Image;
		const radius = 4;
		const marginRadius = 5;
		const strokeWeight = 2;
		const aspectRatio = imageWidth / imageHeight;
		const alpha = 160;
		const markers = {
			tragus: p.createVector(tragus.x, tragus.y),
			shoulder: p.createVector(shoulder.x, shoulder.y),
			waist: p.createVector(waist.x, waist.y)
		};
		let target: p5.Vector | null = null;
		let lastTarget: p5.Vector | null = null;
		const imageOffset: p5.Vector = p.createVector(0, 0);
		let pMouse: p5.Vector | null = null;

		p.preload = () => {
			img = p.loadImage(imageSrc);
		};

		p.setup = () => {
			const container = document.getElementById('annotater-container');
			const height = container?.clientHeight ?? 600;
			const width = Math.min(container?.clientWidth ?? 400, (height * 16) / 9);
			p.createCanvas(width, height);
			p.imageMode(p.CENTER);
			imageOffset.set(0, 0);
			initMarkerPosition();
			adjustFrame([markers.tragus, markers.shoulder, markers.waist]);
			if (holdShoulder && !isMobile) {
				target = markers.shoulder;
			}
		};

		p.draw = () => {
			p.background(255);
			p.push();
			p.translate(p.width / 2 + imageOffset.x, p.height / 2 + imageOffset.y);
			p.scale(scale);
			p.image(img, 0, 0, aspectRatio * p.height, p.height);
			drawMarks();
			p.pop();

			if (target !== null || pMouse !== null) {
				mosueMoved();
			}
		};

		p.mousePressed = () => {
			if (isMobile) return;
			mousePressed(p.mouseX, p.mouseY);
		};

		p.mouseReleased = () => {
			if (isMobile) return;
			mouseReseased(p.mouseX, p.mouseY);
		};

		p.touchStarted = () => {
			if (!isMobile) return;
			mousePressed(p.touches[0].x, p.touches[0].y);
		};

		p.touchEnded = () => {
			if (!isMobile) return;
			mouseReseased(p.mouseX, p.mouseY);
		};

		const drawMarks = () => {
			const correctedWaist = p.createVector(
				markers.waist.x - p.width / 2,
				markers.waist.y - p.height / 2
			);
			const correctedShoulder = p.createVector(
				markers.shoulder.x - p.width / 2,
				markers.shoulder.y - p.height / 2
			);
			const correctedTragus = p.createVector(
				markers.tragus.x - p.width / 2,
				markers.tragus.y - p.height / 2
			);
			p.stroke(255, 255, 0);
			p.strokeWeight(strokeWeight / scale);
			p.line(correctedShoulder.x, correctedShoulder.y, correctedTragus.x, correctedTragus.y);
			if (showWaist) {
				p.line(correctedWaist.x, correctedWaist.y, correctedShoulder.x, correctedShoulder.y);
			}

			p.noStroke();
			p.fill(255, 0, 0, alpha);
			p.ellipse(correctedTragus.x, correctedTragus.y, (radius * 2) / scale, (radius * 2) / scale);
			p.fill(0, 255, 0, alpha);
			p.ellipse(
				correctedShoulder.x,
				correctedShoulder.y,
				(radius * 2) / scale,
				(radius * 2) / scale
			);
			if (showWaist) {
				p.fill(0, 0, 255, alpha);
				p.ellipse(correctedWaist.x, correctedWaist.y, (radius * 2) / scale, (radius * 2) / scale);
			}
		};

		const mousePressed = (x: number, y: number) => {
			if (!mouseInCanvas(x, y)) return;

			const mouse = p.createVector(
				(x - imageOffset.x - p.width / 2) / scale + p.width / 2,
				(y - imageOffset.y - p.height / 2) / scale + p.height / 2
			);
			if (target === null) {
				setTarget(mouse);
				if (target === null && pMouse === null) {
					pMouse = p.createVector(x, y);
					handleSubmit();
				}
			}
		};

		const mosueMoved = () => {
			if (target === null && pMouse === null) return;

			const mouse = p.createVector(
				(p.mouseX - imageOffset.x - p.width / 2) / scale + p.width / 2,
				(p.mouseY - imageOffset.y - p.height / 2) / scale + p.height / 2
			);
			updateMarkPosition(mouse);
			updateImageOffset();
			if (pMouse !== null) {
				pMouse.set(p.mouseX, p.mouseY);
			}
		};

		const mouseReseased = (x: number, y: number) => {
			if (!mouseInCanvas(x, y)) return;

			lastTarget = target;
			target = null;
			pMouse = null;
		};

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
			syncronizeMarkers();
		};

		const initMarkerPosition = (_target?: 'tragus' | 'shoulder' | 'waist') => {
			const len = p.height / 6;
			const initWaist = () => {
				if (posture.waistX && posture.waistY) {
					const { x, y } = adjustMarkerPosition(posture.waistX, posture.waistY);
					markers.waist.set(x, y);
				} else {
					markers.waist.set(p.width / 2, (p.height * 2) / 3);
				}
			};
			const initShoulder = () => {
				if (posture.shoulderX && posture.shoulderY) {
					const { x, y } = adjustMarkerPosition(posture.shoulderX, posture.shoulderY);
					markers.shoulder.set(x, y);
				} else {
					markers.shoulder.set(
						markers.waist.x - len * p.sin(p.radians(correctedTorsoAngle)),
						markers.waist.y - len * p.cos(p.radians(correctedTorsoAngle))
					);
				}
			};
			const initTragus = () => {
				if (posture.tragusX && posture.tragusY) {
					const { x, y } = adjustMarkerPosition(posture.tragusX, posture.tragusY);
					markers.tragus.set(x, y);
				} else {
					markers.tragus.set(
						markers.shoulder.x - len * p.sin(p.radians(correctedNeckAngle)),
						markers.shoulder.y - len * p.cos(p.radians(correctedNeckAngle))
					);
				}
			};

			switch (_target) {
				case 'waist':
					initWaist();
					break;
				case 'shoulder':
					initShoulder();
					break;
				case 'tragus':
					initTragus();
					break;
				default:
					initWaist();
					initShoulder();
					initTragus();
					break;
			}
		};

		const resetTarget = () => {
			const _target = target ?? lastTarget;
			if (_target == markers.tragus) {
				initMarkerPosition('tragus');
			} else if (_target == markers.shoulder) {
				initMarkerPosition('shoulder');
			} else if (_target == markers.waist) {
				initMarkerPosition('waist');
			}
			target = null;
		};

		const mouseInCanvas = (x: number, y: number): boolean => {
			return x >= 0 && x <= p.width && y >= 0 && y <= p.height;
		};

		const distToMouse = (
			point: Vector,
			mouse: p5.Vector = p.createVector(p.mouseX, p.mouseY)
		): number => {
			return p.dist(point.x, point.y, mouse.x, mouse.y);
		};

		const setTarget = (mouse: p5.Vector = p.createVector(p.mouseX, p.mouseY)) => {
			const r = radius + marginRadius;
			if (distToMouse(markers.tragus, mouse) < r) {
				target = markers.tragus;
			} else if (distToMouse(markers.shoulder, mouse) < r) {
				target = markers.shoulder;
			} else if (distToMouse(markers.waist, mouse) < r) {
				target = markers.waist;
			}
		};

		const updateMarkPosition = (mouse: p5.Vector = p.createVector(p.mouseX, p.mouseY)) => {
			if (target === null) return;

			target.add(mouse.x - target.x, mouse.y - target.y);
			syncronizeMarkers();
			correctedNeckAngle = calcAngle(shoulder, tragus);
			correctedTorsoAngle = calcAngle(waist, shoulder);
		};

		const syncronizeMarkers = () => {
			const _tragus = adjustMarkerPositionInverse(markers.tragus.x, markers.tragus.y);
			tragus.x = _tragus.x;
			tragus.y = _tragus.y;
			const _shoulder = adjustMarkerPositionInverse(markers.shoulder.x, markers.shoulder.y);
			shoulder.x = _shoulder.x;
			shoulder.y = _shoulder.y;
			const _waist = adjustMarkerPositionInverse(markers.waist.x, markers.waist.y);
			waist.x = _waist.x;
			waist.y = _waist.y;
		};

		const calcAngle = (p1: Vector, p2: Vector): number => {
			const sign = p1.x < p2.x ? -1 : 1;
			const theta = p.acos(((p2.y - p1.y) * -p1.y) / (p.dist(p1.x, p1.y, p2.x, p2.y) * p1.y));
			return p.degrees(theta * sign);
		};

		const updateImageOffset = (mouse: p5.Vector = p.createVector(p.mouseX, p.mouseY)) => {
			if (pMouse === null) return;

			imageOffset.add(mouse.x - pMouse.x, mouse.y - pMouse.y);
		};

		const adjustMarkerPosition = (x: number, y: number): Vector => {
			const rate = p.height / imageHeight;
			const imageLeft = p.width / 2 - (imageWidth / 2) * rate;
			const imageTop = p.height / 2 - (imageHeight / 2) * rate;
			return {
				x: imageLeft + x * rate,
				y: imageTop + y * rate
			};
		};

		const adjustMarkerPositionInverse = (x: number, y: number): Vector => {
			const rate = p.height / imageHeight;
			const imageLeft = p.width / 2 - (imageWidth / 2) * rate;
			const imageTop = p.height / 2 - (imageHeight / 2) * rate;
			return {
				x: (x - imageLeft) / rate,
				y: (y - imageTop) / rate
			};
		};

		const adjustFrame = (points: Vector[], margin: number = 0.5) => {
			const left = Math.min(...points.map((p) => p.x));
			const right = Math.max(...points.map((p) => p.x));
			const top = Math.min(...points.map((p) => p.y));
			const bottom = Math.max(...points.map((p) => p.y));
			const w = right - left;
			const h = bottom - top;
			const g = p.createVector(
				points.reduce((acc, p) => acc + p.x, 0) / points.length,
				points.reduce((acc, p) => acc + p.y, 0) / points.length
			);
			const rate = Math.min(
				(p.height * aspectRatio) / (w * (1 + margin)),
				p.height / (h * (1 + margin))
			);
			scale = rate;
			imageOffset.set(-(g.x - p.width / 2) * rate, -(g.y - p.height / 2) * rate);
		};
	};
</script>

<div id="annotater-container">
	<div>
		<p>
			<span>首の角度: {correctedNeckAngle.toFixed(2)}</span>
			<br />
			{#if showWaist}
				<span>胴体の角度: {correctedTorsoAngle.toFixed(2)}</span>
			{/if}
		</p>
		<!-- <Button on:click={handleSubmit}>
            <Label>保存</Label>
        </Button> -->
		<div class="view-controller">
			<IconButton class="material-icons" on:click={decrementScale}>remove</IconButton>
			<TextField
				class="input"
				type="number"
				input$step={step}
				value={scale.toFixed(2)}
				on:change={handleInputChange}
			/>
			<IconButton class="material-icons" on:click={incrementScale}>add</IconButton>
		</div>
	</div>
	<P5 {sketch} />
</div>

<style lang="scss" scoped>
	@import '$lib/styles/variables.scss';
	#annotater-container {
		text-align: center;
		width: 100vw;
		height: 80vh;
		overflow: hidden;

		div {
			text-align: center;
			margin: 8px;
		}
	}
	.view-controller :global(input[type='number']) {
		text-align: right;
	}

	canvas {
		user-select: none;
		-webkit-user-select: none;
		-moz-user-select: none;
	}
</style>
