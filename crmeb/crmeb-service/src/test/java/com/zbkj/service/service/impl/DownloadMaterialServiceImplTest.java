package com.zbkj.service.service.impl;

import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.download.DownloadMaterial;
import com.zbkj.service.dao.DownloadMaterialDao;
import com.zbkj.service.service.DownloadMaterialRecordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DownloadMaterialServiceImplTest {

    @Mock
    private DownloadMaterialDao dao;

    @Mock
    private DownloadMaterialRecordService downloadMaterialRecordService;

    @InjectMocks
    private DownloadMaterialServiceImpl downloadMaterialService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(downloadMaterialService, "dao", dao);
    }

    @Test
    public void getDownloadUrlShouldReturnUrlAndIncreaseCountWhenMaterialIsPublic() {
        DownloadMaterial material = new DownloadMaterial();
        material.setId(1);
        material.setFileUrl("/uploads/files/manual.pdf");
        material.setNeedForm(false);
        material.setIsPublic(true);
        material.setStatus(true);
        material.setDownloadCount(3);
        when(dao.selectById(1)).thenReturn(material);

        String url = downloadMaterialService.getDownloadUrl(1, null);

        Assertions.assertEquals("/uploads/files/manual.pdf", url);
        Assertions.assertEquals(Integer.valueOf(4), material.getDownloadCount());
        verify(dao).updateById(material);
        verify(downloadMaterialRecordService, never()).existsByMaterialIdAndPhone(any(), any());
    }

    @Test
    public void getDownloadUrlShouldRejectNeedFormMaterialWhenPhoneIsMissing() {
        DownloadMaterial material = new DownloadMaterial();
        material.setId(2);
        material.setNeedForm(true);
        material.setIsPublic(true);
        material.setStatus(true);
        when(dao.selectById(2)).thenReturn(material);

        Assertions.assertThrows(CrmebException.class, () -> downloadMaterialService.getDownloadUrl(2, null));
    }

    @Test
    public void getDownloadUrlShouldRejectNeedFormMaterialWhenRecordDoesNotExist() {
        DownloadMaterial material = new DownloadMaterial();
        material.setId(3);
        material.setNeedForm(true);
        material.setIsPublic(true);
        material.setStatus(true);
        when(dao.selectById(3)).thenReturn(material);
        when(downloadMaterialRecordService.existsByMaterialIdAndPhone(3, "13800138000")).thenReturn(false);

        Assertions.assertThrows(CrmebException.class, () -> downloadMaterialService.getDownloadUrl(3, "13800138000"));
    }

    @Test
    public void getDownloadUrlShouldReturnUrlWhenNeedFormMaterialHasRecord() {
        DownloadMaterial material = new DownloadMaterial();
        material.setId(4);
        material.setFileUrl("/uploads/files/spec.docx");
        material.setNeedForm(true);
        material.setIsPublic(true);
        material.setStatus(true);
        material.setDownloadCount(0);
        when(dao.selectById(4)).thenReturn(material);
        when(downloadMaterialRecordService.existsByMaterialIdAndPhone(4, "13800138000")).thenReturn(true);

        String url = downloadMaterialService.getDownloadUrl(4, "13800138000");

        Assertions.assertEquals("/uploads/files/spec.docx", url);
        Assertions.assertEquals(Integer.valueOf(1), material.getDownloadCount());
        verify(downloadMaterialRecordService).existsByMaterialIdAndPhone(4, "13800138000");
        verify(dao).updateById(eq(material));
    }
}
