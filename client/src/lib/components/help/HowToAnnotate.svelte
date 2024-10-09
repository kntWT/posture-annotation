<script lang="ts">
	import Paper, { Title, Content, Subtitle } from '@smui/paper';
	import Accordion, { Header, Content as AccordionContent, Panel } from '@smui-extra/accordion';
	import { onMount, type ComponentType } from 'svelte';
	import List, { Item } from '@smui/list';
	import Divider from '$lib/components/common/Divider.svelte';
	import Adjust from './topics/Adjust.svelte';
	import Send from './topics/Send.svelte';
	import Modify from './topics/Modify.svelte';
	import Trouble from './topics/Trouble.svelte';

	export let topic: string;
	export let showTableOfContents = true;

	onMount(() => {
		jumpToTopic(topic);
	});

	const dispatchHandler = (e: CustomEvent<string>) => {
		jumpToTopic(e.detail);
	};

	const jumpToTopic = (topic: string) => {
		const topicEl = Array.from(document.querySelectorAll('.topic-header')).find(
			(el) => el.getAttribute('id') === getTopicId(topic)
		);
		if (topicEl) {
			topicEl.scrollIntoView({ behavior: 'smooth' });
		}
	};

	const getTopicId = (topic: string) => `how-to-annotate-${topic}__header`;

	const contents: {
		title: string;
		topic: string;
		content: ComponentType;
		open: boolean;
	}[] = [
		{
			title: 'マーカ位置の調整',
			topic: 'adjust',
			content: Adjust,
			open: true
		},
		{
			title: 'データ送信',
			topic: 'send',
			content: Send,
			open: true
		},
		{
			title: 'データの修正',
			topic: 'modify',
			content: Modify,
			open: true
		},
		{
			title: '挙動がおかしくなった時',
			topic: 'trouble',
			content: Trouble,
			open: true
		}
	];
</script>

<Paper>
	<Title>アノテーション方法</Title>
	{#if showTableOfContents}
		<Subtitle>目次</Subtitle>
		<List>
			{#each contents as content}
				<Item on:click={() => jumpToTopic(content.topic)}>#{content.title}</Item>
			{/each}
		</List>
		<Divider color="gray" />
	{/if}
	<Content>
		<Accordion multiple>
			{#each contents as content}
				<Panel
					bind:open={content.open}
					variant="outlined"
					color="primary"
					nonInteractive
					class="topic-header"
					id={getTopicId(content.topic)}
				>
					<Header>{content.title}</Header>
					<AccordionContent>
						<svelte:component this={content.content} on:jumpToTopic={dispatchHandler} />
					</AccordionContent>
				</Panel>
			{/each}
		</Accordion>
	</Content>
</Paper>

<style lang="scss" scoped>
	:global(.divider) {
		margin: 24px auto !important;
	}

	:global(.smui-accordion__panel) {
		margin-bottom: 24px;
		:global(.smui-accordion__header__title) {
			font-weight: bold;
		}
	}
</style>
