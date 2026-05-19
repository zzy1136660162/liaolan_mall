package com.zbkj.service.service.impl;

import com.zbkj.common.model.article.ArticleSeoExtend;
import com.zbkj.common.request.ArticleSeoInfoRequest;
import com.zbkj.service.dao.ArticleSeoExtendDao;
import com.zbkj.service.service.SystemAttachmentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class ArticleSeoExtendServiceImplTest {

    private ArticleSeoExtendServiceImpl service;

    private ArticleSeoExtendDao articleSeoExtendDao;

    private SystemAttachmentService systemAttachmentService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        articleSeoExtendDao = Mockito.mock(ArticleSeoExtendDao.class);
        systemAttachmentService = Mockito.mock(SystemAttachmentService.class);
        service = Mockito.spy(new ArticleSeoExtendServiceImpl());
        inject(service, "dao", articleSeoExtendDao);
        inject(service, "systemAttachmentService", systemAttachmentService);
    }

    @Test
    public void saveOrUpdateByArticleId_shouldCreateRecordWhenMissing() {
        ArticleSeoInfoRequest request = new ArticleSeoInfoRequest();
        request.setSeoTitle("SEO title");
        request.setSeoKeywords("cable,news");
        request.setSeoDescription("SEO description");
        request.setShareTitle("Share title");
        request.setShareImage("https://cdn.example.com/crmebimage/upload/share.png");

        Mockito.doReturn(null).when(service).getByArticleId(100);
        Mockito.when(systemAttachmentService.clearPrefix(request.getShareImage()))
                .thenReturn("crmebimage/upload/share.png");

        AtomicReference<ArticleSeoExtend> savedRef = new AtomicReference<>();
        Mockito.doAnswer(invocation -> {
            savedRef.set(invocation.getArgument(0));
            return true;
        }).when(service).save(Mockito.any(ArticleSeoExtend.class));

        Assert.assertTrue(service.saveOrUpdateByArticleId(100, request));

        ArticleSeoExtend saved = savedRef.get();
        Assert.assertNotNull(saved);
        Assert.assertEquals(Integer.valueOf(100), saved.getArticleId());
        Assert.assertEquals("SEO title", saved.getSeoTitle());
        Assert.assertEquals("cable,news", saved.getSeoKeywords());
        Assert.assertEquals("SEO description", saved.getSeoDescription());
        Assert.assertEquals("Share title", saved.getShareTitle());
        Assert.assertEquals("crmebimage/upload/share.png", saved.getShareImage());
        Assert.assertNotNull(saved.getCreateTime());
        Assert.assertNotNull(saved.getUpdateTime());
    }

    @Test
    public void saveOrUpdateByArticleId_shouldUpdateExistingRecord() {
        ArticleSeoExtend existing = new ArticleSeoExtend();
        existing.setId(9);
        existing.setArticleId(100);
        existing.setCreateTime(new Date(123456789L));

        ArticleSeoInfoRequest request = new ArticleSeoInfoRequest();
        request.setSeoTitle("Updated SEO title");
        request.setSeoKeywords("updated");
        request.setSeoDescription("Updated description");
        request.setShareTitle("Updated share title");
        request.setShareImage("https://cdn.example.com/crmebimage/upload/share-2.png");

        Mockito.doReturn(existing).when(service).getByArticleId(100);
        Mockito.when(systemAttachmentService.clearPrefix(request.getShareImage()))
                .thenReturn("crmebimage/upload/share-2.png");

        AtomicReference<ArticleSeoExtend> updatedRef = new AtomicReference<>();
        Mockito.doAnswer(invocation -> {
            updatedRef.set(invocation.getArgument(0));
            return true;
        }).when(service).updateById(Mockito.any(ArticleSeoExtend.class));

        Assert.assertTrue(service.saveOrUpdateByArticleId(100, request));

        ArticleSeoExtend updated = updatedRef.get();
        Assert.assertNotNull(updated);
        Assert.assertEquals(Integer.valueOf(9), updated.getId());
        Assert.assertEquals(Integer.valueOf(100), updated.getArticleId());
        Assert.assertEquals(existing.getCreateTime(), updated.getCreateTime());
        Assert.assertEquals("Updated SEO title", updated.getSeoTitle());
        Assert.assertEquals("updated", updated.getSeoKeywords());
        Assert.assertEquals("Updated description", updated.getSeoDescription());
        Assert.assertEquals("Updated share title", updated.getShareTitle());
        Assert.assertEquals("crmebimage/upload/share-2.png", updated.getShareImage());
        Assert.assertNotNull(updated.getUpdateTime());
    }

    @Test
    public void saveOrUpdateByArticleId_shouldIgnoreEmptyPayloadWhenRecordMissing() {
        Mockito.doReturn(null).when(service).getByArticleId(100);
        Mockito.when(articleSeoExtendDao.selectCount(Mockito.any())).thenReturn(0);

        Assert.assertTrue(service.saveOrUpdateByArticleId(100, null));

        Mockito.verify(service, Mockito.never()).save(Mockito.any(ArticleSeoExtend.class));
        Mockito.verify(service, Mockito.never()).updateById(Mockito.any(ArticleSeoExtend.class));
    }

    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
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
