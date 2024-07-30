<script lang="ts">
	import { goto } from "$app/navigation";
	import Button, { Label } from "@smui/button";
	import List, { Item, Text } from "@smui/list";
	import Paper, { Content, Subtitle } from "@smui/paper";

    const navigateToLogs = () => {
        goto("/logs");
    }

</script>

<Paper>
    <Subtitle>直前のデータの修正したい場合</Subtitle>
    <Content>
        <List nonInteractive>
            <Item>
                <Text>画像上ぶの「<strong>直前に戻る</strong>」ボタンをクリックすることで修正できます</Text>
            </Item>
            <Item wrapper nonInteractive>
                <Item nonInteractive>
                    <Text>リロードすると履歴が消えてしまうのでご注意ください</Text>
                </Item>
            </Item>
        </List>
    </Content>
    <Subtitle>それ以外のデータの修正したい場合</Subtitle>
    <Content>
        <List nonInteractive>
            <Item>
                <Text>
                    ページ左上左上のメニューから
                    <Button on:click={navigateToLogs} ><Label>アノテーション履歴</Label></Button>
                    ページへアクセスしてください
                </Text>
            </Item>
            <Item>
                <Text>過去のアノテーション一覧が表示されているので，該当のアノテーションをクリックすることで修正でます</Text>
            </Item>
            <Item>
                <Text>データを修正したのに反映されていない場合は，スーパーリロード（<code>cmd+shift+r</code>または<code>ctl+F5</code>）をしてみてください</Text>
            </Item>
        </List>
        <img src="/annotation_logs.png" style="width: 100%;" />
    </Content>
</Paper>
