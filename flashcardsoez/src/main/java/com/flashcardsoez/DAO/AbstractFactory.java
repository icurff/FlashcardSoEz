package com.flashcardsoez.DAO;

import java.util.List;

public interface AbstractFactory<T> {

    public void add(T t);

    public void update(T t);

    public void delete(T t);

    public List<T> getList();

    public T getById(int id);
}
