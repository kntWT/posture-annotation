## posture-annotation

**〜概要〜**

- 画像から手動で首の角度をアノテーションするシステム

**システム構成**

- 開発環境
  - Docker
    - Docker上でSvelte + TypeScript，Java，PostgresQL，PGAdminを動かしてる
- フロントエンド
  - [Svelte](https://svelte.dev/)
  - [TypeScript](https://www.typescriptlang.org/)
- バックエンド
  - [Java](https://www.java.com/)
    - フレームワーク：[Spring Boot](https://spring.io/)
- データベース
  - [PostgresQL](https://www.postgresql.org/)

### 設定ファイル
- `./.env`
  - ```
    POSTGRES_HOST=POSTGRES:5432
    POSTGRES_DATABASE=posture_annotation_db
    POSTGRES_USER=ユーザ名
    POSTGRES_PASSWORD=パスワード
    POSTGRES_ROOT_PASSWORD=パスワード
    POSTGRES_INITDB_ARGS=--encoding=UTF-8 --locale=C
    PMA_HOST=db
    PMA_USER=ユーザ名
    PMA_PASSWORD=パスワード
    TZ=Asia/Tokyo

    BUILD_MODE=dev

    IMAGE_DIR=images
    ORIGINAL_IMAGE_DIR=images/original

    API_URL=http://api:8000
    API_ENDPOINT=/api/
    ```
  - 開発用と本番環境用でビルドの方法を変えるため，Dockerfileを分けています
  - apiは，コンテナに入り`mvn compile`を実行すると変更が反映されます

### 開発で使用するポート一覧

|     | port | 説明                           | docker container 名 |
| :-: | ---- | :----------------------------- | ------------------- |
|     | 7150 | データベース，PostgresQL            | posture-annotation-db       |
|     | 7151 | SQLクライアント，PGAdmin                        | posture-annotation-pgadmin          |
|     | 7152 | API，Java         | posture-annotation-api        |
|     | 7153 | クライアント，SvelteKit | posture-annotation-client   |
