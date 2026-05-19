# Agent Status

## Scope

Implemented the company-news SEO extension with:

- a separate article SEO extension table
- backend persistence and response assembly
- admin form support for SEO/share fields
- H5 and share output wiring on the news detail page

## Current State

- Backend code is in place for article SEO/share persistence and fallback behavior.
- Admin article edit now edits `seoInfo` directly instead of always mirroring title/synopsis into share fields.
- App news detail now reads `seoInfo` for share cards and updates H5 `title`, `keywords`, and `description`.
- Targeted backend tests were added and are passing.

## SQL Deployment

Run [`docs/article-seo-extend.md`](/E:/ZZY_PROJECT/wt-news/docs/article-seo-extend.md) before deploying the backend.

Notes:

- The implementation uses `create_time` / `update_time` to match the existing CRMEB schema conventions in this repo.
- No data backfill is required.
- Existing articles continue to work because the backend and frontend fall back to article title, synopsis, and cover image when no extension row exists.

Recommended rollout order:

1. Apply `docs/article-seo-extend.md`.
2. Deploy the Java backend.
3. Deploy the admin frontend.
4. Deploy the app/H5 frontend.
5. Edit one article in admin and verify:
   - `eb_article_seo_extend` has one row for that article
   - H5 page `<title>` updates
   - `meta[name="keywords"]` and `meta[name="description"]` update
   - WeChat share title/image use the new fields

## Affected Files

- `crmeb/crmeb-common/src/main/java/com/zbkj/common/model/article/ArticleSeoExtend.java`
- `crmeb/crmeb-common/src/main/java/com/zbkj/common/request/ArticleRequest.java`
- `crmeb/crmeb-common/src/main/java/com/zbkj/common/request/ArticleSeoInfoRequest.java`
- `crmeb/crmeb-common/src/main/java/com/zbkj/common/response/ArticleResponse.java`
- `crmeb/crmeb-common/src/main/java/com/zbkj/common/response/ArticleSeoInfoResponse.java`
- `crmeb/crmeb-admin/src/main/java/com/zbkj/admin/controller/ArticleController.java`
- `crmeb/crmeb-service/src/main/java/com/zbkj/service/dao/ArticleSeoExtendDao.java`
- `crmeb/crmeb-service/src/main/java/com/zbkj/service/service/ArticleSeoExtendService.java`
- `crmeb/crmeb-service/src/main/java/com/zbkj/service/service/ArticleService.java`
- `crmeb/crmeb-service/src/main/java/com/zbkj/service/service/impl/ArticleSeoExtendServiceImpl.java`
- `crmeb/crmeb-service/src/main/java/com/zbkj/service/service/impl/ArticleServiceImpl.java`
- `crmeb/crmeb-service/src/test/java/com/zbkj/service/service/impl/ArticleSeoExtendServiceImplTest.java`
- `crmeb/crmeb-service/src/test/java/com/zbkj/service/service/impl/ArticleServiceImplTest.java`
- `admin/src/api/article.js`
- `admin/src/views/content/article/edit.vue`
- `app/pages/news/news_details/index.vue`
- `docs/article-seo-extend.md`
- `docs/agent-status.md`

## Verification Notes

- JDK 8 is required for this Maven build on the current machine because the repo still uses Lombok 1.18.12.
- Targeted backend tests passed with:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-1.8'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
mvn -pl crmeb-service -am "-Dtest=ArticleServiceImplTest,ArticleSeoExtendServiceImplTest" "-Dsurefire.failIfNoSpecifiedTests=false" test
```

- Result: `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0`
