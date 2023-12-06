package com.resume.hh_wrapper;

import java.util.List;

public interface ApiClient {

    <T> T get(String uri, Class<T> type);

    <T> List<T> getList(String uri, Class<T> type);

    <T> T post(String uri, T body, Class<T> type);

    <T> T put(String uri, T body, Class<T> type);
}
