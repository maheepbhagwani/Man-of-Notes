package com.example.meep.firebase;

public class Uploads {

        public String name;
        public String url;
        public String owner;
        // Default constructor required for calls to
        // DataSnapshot.getValue(User.class)
        public Uploads() {
        }

        public Uploads(String name, String url) {
            this.name = name;
            this.url = url;
            this.owner= owner;

        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
        public String getOwner() {
            return url;
        }


}
