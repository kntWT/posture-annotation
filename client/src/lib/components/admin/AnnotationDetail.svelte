<script lang="ts">
	import type { Annotation, Posture } from '$api/generated';
	import { formatDate, getHighlightColor, imageUrl } from '$lib/util';
	import Card, { Content, Media } from '@smui/card';

	export let annotation: Annotation;
	export let posture: Posture;

	const DIGIT = 4;

	const formatDiffNeckAngle = (_diffNeckAngle: number, digit: number = DIGIT) => {
		const diffNeckAngle = parseFloat(_diffNeckAngle.toFixed(digit));
		if (diffNeckAngle > 0) {
			return `+${diffNeckAngle}`;
		} else {
			return diffNeckAngle.toString();
		}
	};

	$: src = imageUrl(
		{
			userId: posture.isSample ? 'sample' : posture.userId,
			fileName: `${formatDate(posture.exCreatedAt)}.jpg`,
			annotaterId: annotation.annotaterId
		},
		'annotated'
	);
</script>

<Card>
	<Content>
		<h4>#{annotation.postureId}</h4>
		<p>もとの角度: {posture.neckAngle.toFixed(DIGIT)}</p>
		<p>
			変更後の角度: {annotation.neckAngle.toFixed(DIGIT)}
			<span class={getHighlightColor(annotation.neckAngle - posture.neckAngle, 20)}
				>（{formatDiffNeckAngle(annotation.neckAngle - posture.neckAngle)}）</span
			>
		</p>
	</Content>
	<Media style={`background-image: url(${src})`} aspectRatio="square" />
</Card>

<style lang="scss" scoped>
	@import '$lib/styles/_variables';

	:global(.mdc-card) {
		padding: 16px;

		:global(.smui-card__content) {
			aspect-ratio: 3/1;

			h4,
			p {
				text-align: center;
			}
		}
	}

	:global(.mdc-card__media) {
		background-size: cover;
		background-position: 50% 0%;
	}

	.green {
		background-color: $highlight-green;
	}

	.red {
		background-color: $highlight-red;
	}
</style>
