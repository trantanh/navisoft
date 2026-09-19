package com.trantanh.navipos.service.impl;

import com.trantanh.eet.table.Eet;
import com.trantanh.navipos.dao.BillDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BillServiceImplTest {
    private BillDao billDao;
    private BillServiceImpl service;

    @BeforeEach
    void setUp() {
        billDao = mock(BillDao.class);
        service = new BillServiceImpl(billDao);
    }

    @Test
    void delegatesOfflineBillLoadingToDao() {
        Eet submission = new Eet(42, "202609190001", "19.09.2026", "10:15", "Čeká na odeslání");
        when(billDao.getCurrentBillOfflineList()).thenReturn(List.of(submission));

        assertEquals(List.of(submission), service.getCurrentBillOfflineList());
        verify(billDao).getCurrentBillOfflineList();
    }

    @Test
    void updatesFikUsingInterfaceParameterOrder() {
        service.updateFik("202609190001", "legacy-fik");

        verify(billDao).updateFik("202609190001", "legacy-fik");
    }

    @Test
    void ignoresMissingFik() {
        service.updateFik("202609190001", null);

        verify(billDao, never()).updateFik("202609190001", null);
    }
}
