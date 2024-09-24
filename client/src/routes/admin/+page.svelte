<script lang="ts">
	import Paper, { Title, Content } from '@smui/paper';
	import Card from '@smui/card';
	import List, { Item } from '@smui/list';
	import { goto } from '$app/navigation';

	const navigateTo = (path: 'annotation' | 'user') => {
		goto(`${import.meta.env.VITE_BASE_PATH}/admin/${path}`, { invalidateAll: true });
	};
</script>

<Paper>
	<Title>管理者ページ</Title>
	<Content>
		<List>
			<Item on:click={() => navigateTo('annotation')}>
				<Card>
					<h3>アノテーション</h3>
					<p>アノテーション結果をposture_idごとに集計</p>
				</Card>
			</Item>
			<Item on:click={() => navigateTo('user')}>
				<Card>
					<h3>ユーザー</h3>
					<p>アノテーション結果をユーザごとに集計</p>
				</Card>
			</Item>
		</List>
	</Content>
</Paper>

<style lang="scss" scoped>
	:global(.smui-paper) {
		margin: 16px;

		:global(.mdc-deprecated-list-item) {
			margin: 8px 0;
		}
	}
</style>
