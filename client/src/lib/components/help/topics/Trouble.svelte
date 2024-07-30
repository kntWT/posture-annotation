<script lang="ts">
	import List, { Item, PrimaryText, SecondaryText, Text } from "@smui/list";
	import Paper, { Content } from "@smui/paper";

</script>

<div class="wrapper">
    <Paper>
        <Content>
            データを送信する意図がないのに送信されてしまう，データを送信しても次の画像に切り替わらないなどといった不具合が生じる場合があります．<br/>
            その場合はお手数ですが，以下の方法をお試しください．
            <List threeLine nonInteractive>
                <Item wrapper>
                    <Text>
                        <PrimaryText>ページをリロードする</PrimaryText>
                        <SecondaryText>通常のリロードやスーパーリロードを試してみてください</SecondaryText>
                    </Text>
                </Item>
                <Item wrapper>
                    <Text>
                        <PrimaryText>ページのパスを確認する</PrimaryText>
                        <SecondaryText><code>/annotate</code>が本番用，<code>/annotate/sample</code>が練習用のパスになります</SecondaryText>
                        <SecondaryText>また，<code>?id=xx</code>というようなパラメータがついている場合があります</SecondaryText>
                        <SecondaryText>特定の画像をアノテーションしたい場合以外はこのパラメータを削除してリロードしてみてください</SecondaryText>
                    </Text>
                </Item>
                <Item wrapper>
                    <Text>ブラウザのキャッシュをクリアする</Text>
                </Item>
            </List>
            これらの手順でも解決しない場合，お手数ですが管理者にお問い合わせください．
        </Content>
    </Paper>
</div>

<style lang="scss" scoped>
    .wrapper{

        :global(.smui-paper) {
            padding: 0;
        }

        :global(.mdc-deprecated-list-item__wrapper) {
            padding-left: 16px;
            padding-bottom: 12px;
        }
    }
</style>
