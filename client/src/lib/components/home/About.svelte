<script lang="ts">
	import Paper, { Content, Title } from '@smui/paper';

	const sampleDataSize = import.meta.env.VITE_SAMPLE_DATA_SIZE;
	const prodDataSize = import.meta.env.VITE_PROD_DATA_SIZE;
	const base = import.meta.env.VITE_BASE_PATH;
</script>

<Paper class="about" square={false} elevation={0}>
	<Title>ご協力いただきありがとうございます！</Title>
	<Content>
		<ul>
			<li>
				本システムは，スマートフォンのみを用いた姿勢矯正システムの実現に向けたデータセット構築システムです．
			</li>
			<li>
				提示された画像に対して，肩や耳の位置を指定して正確な首の角度をアノテーションしていただきます．
			</li>
			<li>具体的な方法は「<strong>アノテーション方法</strong>」をご覧ください．</li>
			<li>
				<strong><a href="{base}/annotate/sample">アノテーション練習</a></strong
				>としてまず{sampleDataSize}枚の画像をアノテーションしていただきます．
			</li>
			<li>
				その後，<strong><a href="{base}/annotate/sample">本番用</a></strong
				>として{prodDataSize}枚の画像をアノテーションしていただきます．
			</li>
		</ul>
	</Content>
</Paper>

<style lang="scss" scoped>
	.about {
		padding: 24px;
		margin: 0 auto;
		max-width: 800px;
	}
</style>
