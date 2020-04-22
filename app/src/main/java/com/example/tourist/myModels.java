package com.example.tourist;

public class myModels {

    public class contentModel{
        String recordId, title, content, contentGroup,travel, favourite;
        private byte[] blobpics;
        public contentModel(String title, String recordId, String content,
                            String contentGroup, String travel,
                            String favourite,byte[] blobpics) {
            this.title = title;
            this.recordId = recordId;
            this.content = content;
            this.contentGroup = contentGroup;
            this.travel = travel;
            this.favourite = favourite;
            this.blobpics = blobpics;
        }

        public byte[] getBlobpics() {
            return blobpics;
        }

        public String getRecordId() {
            return recordId;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getContentGroup() {
            return contentGroup;
        }

        public String getTravel() {
            return travel;
        }

        public String getFavourite() {
            return favourite;
        }
    }
}
