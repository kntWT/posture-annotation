<script lang="ts">
    import Textfield from "@smui/textfield";
    import Button from "@smui/button";
    import { z } from "zod";
    import { createForm } from "felte";
    import { validator } from "@felte/validator-zod"
    import { reporter } from '@felte/reporter-svelte';
	import type { UserCreate } from "$api/generated";
    import ValidationMessage from "./ValidationMessage.svelte";

    export let title: string;
    export let action: string;
    export let handleSubmit: (values: UserCreate) => Promise<unknown>;

    const schema = z.object({
        name: z.string()
            .trim()
            .min(1, { message: "名前を入力してください" })
            .max(20, { message: "20文字以内で入力してください" }),
        password: z.string()
            .trim()
            .min(1, { message: "パスワードを入力してください" })
            .max(20, { message: "20文字以内で入力してください" }),
    });

    type schemaType = z.infer<typeof schema>;

    const initialValues: UserCreate = {
        name: "",
        password: "",
    };

    const { form, data, isValid } =  createForm<schemaType>({
        initialValues,
        onSubmit: async (values) => {
            await handleSubmit(values);
        },
        extend: [ validator({ schema }), reporter],
    });
</script>

<div class="form">
    <h1>{title}</h1>
    <form
        use:form
        method="post"
        action={`?/${action}`}
    >
        <div class="form-group">
            <Textfield
                label="ユーザ名"
                bind:value={$data.name}
            />
            <ValidationMessage name="name" />
            <Textfield
                label="パスワード"
                type="password"
                style="suggested: current-password;"
                bind:value={$data.password}
            />
            <ValidationMessage name="password" />
        </div>
        <Button disabled={!$isValid} variant="raised" type="submit" >{title}</Button>
    </form>
</div>

<style lang="scss" scoped>
    .form {
        margin: 20px;
        text-align: center;
    }

    .form-group {
        width: 80%;
        min-width: 200px;
        max-width: 400px;
        margin: 24px auto;

        :global(.mdc-text-field) {
            width: 100%;
        }
    }
</style>