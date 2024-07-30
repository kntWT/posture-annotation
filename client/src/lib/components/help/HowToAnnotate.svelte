<script lang="ts">
    import Paper, { Title, Content, Subtitle } from "@smui/paper";
    import Accordion, { Header, Content as AccordionContent, Panel } from "@smui-extra/accordion";
	import { onMount } from "svelte";
	import List, { Item } from "@smui/list";
	import Divider from "$lib/components/common/Divider.svelte";

    export let topic: string;
    export let showTableOfContents = true;

    onMount(() => {
        jumpToTopic(topic);
    });

    const jumpToTopic = (topic: string) => {
        const topicEl = Array.from(document.querySelectorAll(".topic-header"))
            .find(el => el.getAttribute("id") === getTopicId(topic));
        if (topicEl) {
            topicEl.scrollIntoView({ behavior: "smooth" });
        }
    }

    const getTopicId = (topic: string) => `how-to-annotate-${topic}__header`;

    const contents = [
        {
            title: "マーカ位置の調整",
            topic: "adjust",
            innterHTML: `
                <ul>
                    <li>マーカの位置をドラッグ&ドロップまたは方向キーで調整することができます．</li>
                    <li>方向キーは最後にホールドしたマーカの位置を調整することができます．．</li>
                    <li>また，escapeキーを押すことで直前に操作したマーカの位置をリセットできます．</li>
                    <li>耳と肩を結ぶ黄色い線が首の角度と平行になるようにマーカを調整してください．</li>
                    <li>このとき，<strong>もとの肩の位置に囚われすぎる必要はありません．首の角度と平行にすることを優先してください．</strong></li>
                    <li>ページに訪れたときは肩のマーカがホールドされている状態になるので，適切な位置に調整してクリックしてください．</li>
                    <li>基本的には肩のみを調整すれば首の角度と平行になりますが，必要に応じて耳のマーカも調整してください．</li>
                    <li>腰のマーカについては特に気にする必要はありません．</li>
                    <li>自動的に顔〜腰が映るように画像の倍率を調整していますが，修正が必要な場合は<span class="material-icons">remove</span><span class="material-icons">add</span>ボタンや数字を直接入力して調整してください．</li>
                    <li>マーカの調整が終わったらデータを送信し，次の画像に進んでください．</li>
                </ul>
                <video style="width: 100%" src="/annotation-demo.mp4" autoplay controls loop />`,
            open: true,
        },
        {
            title: "データ送信方法",
            topic: "send",
            innterHTML: `
                <p>データの送信方法は以下の2種類かあります</p>
                <ul>
                    <li><i>Enter</i>キーまたは<i>Space</i>キーを押す</li>
                    <li>表示されている画像中のマーカ以外の部分をクリックする</li>
                </ul>
                <p>
                    データを送信すると，自動で次の画像が表示されます．<br/>
                    もし画像が切り替わらない場合は，データの送信に失敗している場合があります．<strong>挙動がおかしくなった場合</strong>を参照してください．
                </p>`,
            open: true,
        },
        {
            title: "データの修正方法",
            topic: "modify",
            innterHTML: `
                <p>直前のデータの修正したい場合</p>
                <ul>
                    <li>画像上ぶの「<strong>直前に戻る</strong>」ボタンをクリックすることで修正できます．</li>
                    <ul><li>リロードすると履歴が消えてしまうのでご注意ください</li></ul>
                </ul>
                <p>それ以外のデータの修正したい場合</p>
                <ul>
                    <li>ページ左上左上の<span class="material-icons">menu</span>ボタンから「<strong>アノテーション履歴</strong>」ページへアクセスしてください．</li>
                    <li>過去のアノテーション一覧が表示されているので，該当のアノテーションをクリックすることで修正でます．</li>
                    <li>データを修正したのに反映されていない場合は，スーパーリロード（<code>cmd+shift+r</code>または<code>ctl+F5</code>）をしてみてください．</li>
                </ul>
                <img src="/annotation_logs.png" style="width: 100%;" />`,
            open: true,
        },
        {
            title: "挙動がおかしくなった時",
            topic: "trouble",
            innterHTML: `
                <p>
                    データを送信する意図がないのに送信されてしまう，データを送信しても次の画像に切り替わらないなどといった不具合が生じる場合があります．<br/>
                    その場合はお手数ですが，以下の方法をお試しください．
                    <ol>
                        <li>ページをリロードする</li>
                        <ul><li>通常のリロードやスーパーリロードを試してみてください．</li></ul>
                        <li>ページのパスを確認する</li>
                        <ul>
                            <li><code>/annotate</code>が本番用，<code>/annotate/sample</code>が練習用のパスになります．</li>
                            <li>また，<code>?id=xx</code>というようなパラメータがついている場合があります，特定の画像をアノテーションしたい場合以外はこのパラメータを削除してリロードしてみてください．</li>
                        </ul>
                        <li>ブラウザのキャッシュをクリアする</li>
                    </ol>
                    これらの手順でも解決しない場合，お手数ですが管理者にお問い合わせください．
                </p>`,
            open: true,
        }
    ]

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
                        {@html content.innterHTML}
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
