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
  - [nginx](https://nginx.org/en/)
    - 画像配信用
- スキーマ
  - [OpenAPI](https://www.openapis.org/)
- データベース
  - [PostgresQL](https://www.postgresql.org/)

### 設定ファイル
- `./.env`
  - ```
    POSTGRES_HOST=postgres
    POSTGRES_DATABASE=posture_annotation_db
    POSTGRES_USER=
    POSTGRES_PASSWORD=
    POSTGRES_ROOT_PASSWORD=
    POSTGRES_INITDB_ARGS=--encoding=UTF-8 --locale=C
    PGADMIN_DEFAULT_EMAIL=
    PGADMIN_DEFAULT_PASSWORD=
    TZ=Asia/Tokyo
    # pgadminを見るための設定
    SCRIPT_NAME=
    USE_X_FORWARDED_HOST=True
    SECURE_PROXY_SSL_HEADER=HTTP_X_FORWARDED_PROTO,https

    BUILD_MODE=dev|prod
    ORIGIN=

    IMAGE_DIR=src/main/resources/static/images/annotated/
    INITIAL_SAMPLE_DATA_SIZE=100
    GOAL_ANNOTATION_COUNT_PER_POSTURE=

    API_URL=http://api:8000
    API_ENDPOINT=/api/
    ```
  - 開発用と本番環境用でビルドの方法を変えるため，Dockerfileを分けています
  - apiは，コンテナに入り`mvn compile`を実行すると変更が反映されます

### 開発で使用するポート一覧

|     | port | 説明                           | docker container 名 |
| :-: | ---- | :----------------------------- | ------------------- |
|     | 5170 | データベース，PostgresQL            | posture-annotation-postgres       |
|     | 5171 | SQLクライアント，PGAdmin                        | posture-annotation-pgadmin          |
|     | 5172 | API，Java         | posture-annotation-api        |
|     | 5173 | クライアント，SvelteKit | posture-annotation-client   |
|     | 5174 | 画像配信サーバ，nginx | posture-annotation-nginx   |
|     | 5175 | API仕様書，Swagger UI | posture-annotation-swagger-ui   |
