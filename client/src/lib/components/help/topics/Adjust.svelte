<script lang="ts">
	import Button, { Label } from '@smui/button';
	import DataTable, { Head, Body, Row, Cell } from '@smui/data-table';
	import List, { Item, Text } from '@smui/list';
	import Paper, { Content, Subtitle } from '@smui/paper';
	import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	const dispatchJumpToSend = () => {
		dispatch('jumpToTopic', 'send');
	};

	const base = import.meta.env.VITE_BASE_PATH;
</script>

<Paper>
	<Subtitle>操作方法</Subtitle>
	<Content>
		<DataTable style="width: 100%;">
			<Head>
				<Row>
					<Cell>入力</Cell>
					<Cell>動作説明</Cell>
				</Row>
			</Head>
			<Body>
				<Row>
					<Cell>マーカ付近をドラッグ&ドロップ</Cell>
					<Cell>マーカの位置を調整</Cell>
				</Row>
				<Row>
					<Cell>方向キー</Cell>
					<Cell>直前に操作したマーカの位置を調整</Cell>
				</Row>
				<Row>
					<Cell>escapeキー</Cell>
					<Cell>直前に操作したマーカの位置をリセット</Cell>
				</Row>
			</Body>
		</DataTable>
	</Content>
	<Subtitle>注意事項</Subtitle>
	<Content>
		<List nonInteractive>
			<Item>
				<Text
					><strong
						>もとの方の位置やARマーカは無視し，耳と肩を結ぶ黄色い線が首の角度と平行になるようにマーカを調整してください</strong
					></Text
				>
			</Item>
			<Item>
				<Text
					>基本的には肩のみを調整すれば首の角度と平行になりますが，必要に応じて耳のマーカも調整してください</Text
				>
			</Item>
			<Item>
				<Text
					>画像の倍率は必要に応じて<span class="material-icons">remove</span><span
						class="material-icons">add</span
					>ボタンや数字を直接入力して調整してください</Text
				>
			</Item>
			<Item>
				<Text>
					マーカの調整が終わったら
					<Button on:click={dispatchJumpToSend} tag="span">
						<Label>データを送信</Label>
					</Button>
					し，次の画像に進んでください
				</Text>
			</Item>
		</List>
		<video
			style="width: 100%"
			src="{base}/annotation-demo.mp4"
			autoplay
			playsinline
			controls
			loop
		/>
	</Content>
</Paper>

<style lang="scss" scoped>
	:global(.smui-paper) {
		:global(.mdc-data-table) {
			margin-bottom: 24px;
		}
	}
</style>
