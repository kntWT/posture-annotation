<script lang="ts">
	import Button, { Label } from "@smui/button";
    import List, { Item, Text } from "@smui/list";
	import Paper, { Content, Subtitle } from "@smui/paper";
    import { createEventDispatcher } from "svelte";

    const dispatch = createEventDispatcher();
    const jumpToTrouble = () => {
        dispatch("jumpToTopic", "trouble");
    }
</script>

<Paper>
    <Subtitle>データの送信方法</Subtitle>
    <Content>
        <List nonInteractive>
            <Item nonInteractive>
                <Text><i>Enter</i>キーまたは<i>Space</i>キーを押す</Text>
            </Item>
            <Item nonInteractive>
                <Text>マーカ以外の部分をクリックする</Text>
            </Item>
        </List>
    </Content>
    <Subtitle>注意事項</Subtitle>
    <Content>
        <List nonInteractive>
            <Item>
                <Text>データを送信すると，自動で次の画像が表示されます</Text>
            </Item>
            <Item>
                <Text>
                    もし画像が切り替わらない場合は，データの送信に失敗している場合があります<br/>
                    <Button on:click={jumpToTrouble} tag="span" ><Label>挙動がおかしくなった時</Label></Button>を参照してください
                </Text>
            </Item>
        </List>
    </Content>
</Paper>
