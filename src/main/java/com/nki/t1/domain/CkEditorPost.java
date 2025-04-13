package com.nki.t1.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public interface CkEditorPost {
    Integer getPostId();
    String getTitle();
    String getContent();
    void setContentTxt(String content);

    default void extractContentTxtFromContent() {
        Document doc = Jsoup.parse(this.getContent());
        String contentTxt = doc.text();
        setContentTxt(contentTxt);
    }
}
