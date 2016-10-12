package sodevan.adtouch;

import java.util.List;


/**
 * Created by Devanshu on 12-10-2016.
 */
public class AdsParser {

    /**
     * Result : [{"Title":"First Ad","Image":"http://www.hackveda.in/adtouch/zomato.png","Data":[{"Text":"AdText1","Image":"http://www.adtouch.in/images/ad1.jpg"},{"Text":"AdText2","Image":"http://www.adtouch.in/images/ad2.jpg"},{"Text":"AdText3","Image":"http://www.adtouch.in/images/ad3.jpg"},{"Text":"AdText4","Image":"http://www.adtouch.in/images/ad4.jpg"},{"Text":"AdText5","Image":"http://www.adtouch.in/images/ad5.jpg"}]}]
     */

    private List<ResultEntity> Result;

    public void setResult(List<ResultEntity> Result) {
        this.Result = Result;
    }

    public List<ResultEntity> getResult() {
        return Result;
    }

    public static class ResultEntity {
        /**
         * Title : First Ad
         * Image : http://www.hackveda.in/adtouch/zomato.png
         * Data : [{"Text":"AdText1","Image":"http://www.adtouch.in/images/ad1.jpg"},{"Text":"AdText2","Image":"http://www.adtouch.in/images/ad2.jpg"},{"Text":"AdText3","Image":"http://www.adtouch.in/images/ad3.jpg"},{"Text":"AdText4","Image":"http://www.adtouch.in/images/ad4.jpg"},{"Text":"AdText5","Image":"http://www.adtouch.in/images/ad5.jpg"}]
         */

        private String Title;
        private String Image;
        private List<DataEntity> Data;

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

        public void setData(List<DataEntity> Data) {
            this.Data = Data;
        }

        public String getTitle() {
            return Title;
        }

        public String getImage() {
            return Image;
        }

        public List<DataEntity> getData() {
            return Data;
        }

        public static class DataEntity {
            /**
             * Text : AdText1
             * Image : http://www.adtouch.in/images/ad1.jpg
             */

            private String Text;
            private String Image;

            public void setText(String Text) {
                this.Text = Text;
            }

            public void setImage(String Image) {
                this.Image = Image;
            }

            public String getText() {
                return Text;
            }

            public String getImage() {
                return Image;
            }
        }
    }
}
