# Article SEO Extend SQL

Apply this before deploying the backend:

```sql
CREATE TABLE IF NOT EXISTS `eb_article_seo_extend` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `article_id` bigint NOT NULL COMMENT 'Article id',
  `seo_title` varchar(255) DEFAULT NULL COMMENT 'SEO title',
  `seo_keywords` varchar(255) DEFAULT NULL COMMENT 'SEO keywords',
  `seo_description` varchar(500) DEFAULT NULL COMMENT 'SEO description',
  `share_title` varchar(255) DEFAULT NULL COMMENT 'Share title',
  `share_image` varchar(255) DEFAULT NULL COMMENT 'Share image',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_id` (`article_id`),
  KEY `idx_update_time` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Article SEO extension';
```
