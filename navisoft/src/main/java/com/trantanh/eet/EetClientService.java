package com.trantanh.eet;

import com.trantanh.eet.model.RequestEet;
import com.trantanh.eet.model.ResponseEet;
/**
 *
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public interface EetClientService {
    ResponseEet sendEet(RequestEet requestEet);
}
