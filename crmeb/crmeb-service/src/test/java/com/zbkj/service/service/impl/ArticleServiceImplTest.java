package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.article.Article;
import com.zbkj.common.model.article.ArticleSeoExtend;
import com.zbkj.common.request.ArticleRequest;
import com.zbkj.common.request.ArticleSeoInfoRequest;
import com.zbkj.common.response.ArticleResponse;
import com.zbkj.service.dao.ArticleDao;
import com.zbkj.service.service.ArticleSeoExtendService;
import com.zbkj.service.service.SystemAttachmentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

public class ArticleServiceImplTest {

    private ArticleServiceImpl service;

    private ArticleDao articleDao;

    private ArticleSeoExtendService articleSeoExtendService;

    private SystemAttachmentService systemAttachmentService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        articleDao = Mockito.mock(ArticleDao.class);
        articleSeoExtendService = Mockito.mock(ArticleSeoExtendService.class);
        systemAttachmentService = Mockito.mock(SystemAttachmentService.class);
        service = Mockito.spy(new ArticleServiceImpl());
        inject(service, "dao", articleDao);
        inject(service, "articleSeoExtendService", articleSeoExtendService);
        inject(service, "systemAttachmentService", systemAttachmentService);
        injectBaseMapper(service, articleDao);
    }

    @Test
    public void getVoByFront_shouldMergeSeoInfoAndFallbacks() {
        Article article = new Article();
        article.setId(10);
        article.setTitle("Company News");
        article.setSynopsis("Default synopsis");
        article.setShareTitle("Legacy share title");
        article.setShareSynopsis("Legacy share synopsis");
        article.setImageInput("crmebimage/upload/news.png");
        article.setContent("<p>content</p>");
        article.setVisit("7");
        article.setProductId(23);
        article.setStatus(false);

        ArticleSeoExtend seoExtend = new ArticleSeoExtend();
        seoExtend.setArticleId(10);
        seoExtend.setSeoTitle("SEO title");
        seoExtend.setSeoKeywords("wire,cable");
        seoExtend.setSeoDescription("SEO description");

        Mockito.when(articleDao.selectById(10)).thenReturn(article);
        Mockito.when(articleSeoExtendService.getByArticleId(10)).thenReturn(seoExtend);

        ArticleResponse response = service.getVoByFront(10);

        Assert.assertEquals(Integer.valueOf(23), response.getProductId());
        Assert.assertNotNull(response.getSeoInfo());
        Assert.assertEquals("SEO title", response.getSeoInfo().getSeoTitle());
        Assert.assertEquals("wire,cable", response.getSeoInfo().getSeoKeywords());
        Assert.assertEquals("SEO description", response.getSeoInfo().getSeoDescription());
        Assert.assertEquals("Legacy share title", response.getSeoInfo().getShareTitle());
        Assert.assertEquals("crmebimage/upload/news.png", response.getSeoInfo().getShareImage());

        ArgumentCaptor<Article> captor = ArgumentCaptor.forClass(Article.class);
        Mockito.verify(articleDao).updateById(captor.capture());
        Assert.assertEquals("8", captor.getValue().getVisit());
    }

    @Test
    public void create_shouldPersistArticleAndSeoInfo() {
        ArticleSeoInfoRequest seoInfo = new ArticleSeoInfoRequest();
        seoInfo.setSeoTitle("SEO title");
        seoInfo.setSeoKeywords("wire,cable");
        seoInfo.setSeoDescription("SEO description");
        seoInfo.setShareTitle("Share title");
        seoInfo.setShareImage("https://cdn.example.com/crmebimage/upload/share.png");

        ArticleRequest request = new ArticleRequest();
        request.setAuthor("Editor");
        request.setCid("12");
        request.setTitle("Company News");
        request.setSynopsis("Default synopsis");
        request.setImageInput("https://cdn.example.com/crmebimage/upload/news.png");
        request.setContent("<p>content</p>");
        request.setIsBanner(false);
        request.setIsHot(true);
        request.setSeoInfo(seoInfo);

        Mockito.when(systemAttachmentService.clearPrefix(request.getImageInput()))
                .thenReturn("crmebimage/upload/news.png");
        Mockito.when(systemAttachmentService.clearPrefix(request.getContent()))
                .thenReturn("<p>content</p>");
        Mockito.doAnswer(invocation -> {
            Article article = invocation.getArgument(0);
            article.setId(200);
            return true;
        }).when(service).save(Mockito.any(Article.class));
        Mockito.when(articleSeoExtendService.saveOrUpdateByArticleId(200, seoInfo)).thenReturn(true);

        Assert.assertTrue(service.create(request));

        ArgumentCaptor<Article> captor = ArgumentCaptor.forClass(Article.class);
        Mockito.verify(service).save(captor.capture());
        Article saved = captor.getValue();
        Assert.assertEquals("crmebimage/upload/news.png", saved.getImageInput());
        Assert.assertEquals("SEO description", saved.getShareSynopsis());
        Assert.assertEquals("Share title", saved.getShareTitle());
        Assert.assertEquals("0", saved.getVisit());
        Mockito.verify(articleSeoExtendService).saveOrUpdateByArticleId(200, seoInfo);
    }

    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void injectBaseMapper(Object target, BaseMapper<?> mapper) throws Exception {
        Field field = findField(target.getClass(), "baseMapper");
        field.setAccessible(true);
        field.set(target, mapper);
    }

    private Field findField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}
