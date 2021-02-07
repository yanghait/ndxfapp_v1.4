package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;


import java.util.List;

/**
 * author hbzhou
 * date 2019/1/22 11:42
 */
public class InspectionCreateTaskGroupBean {

    /**
     * treeId : 1
     * title : 消防给水系统
     * parentId : 0
     * spread : false
     * checkArr : [{"type":"0","isChecked":"0"}]
     * children : [{"treeId":"340c6cb89dd3466980d80c8544db595f","title":"检查控制阀门的启闭状态","parentId":"1","spread":true,"checkArr":[{"type":"0","isChecked":"0"}],"children":[]},{"treeId":"5a7d8c0dac0c4aa2883ee7cc1701c537","title":"测试消防水泵手动/自动启泵功能和主、备泵切换功能","parentId":"1","spread":true,"checkArr":[{"type":"0","isChecked":"0"}],"children":[]}]
     */

    private String treeId;
    private String title;
    private String parentId;
    private boolean spread;
    private List<CheckArrBean> checkArr;
    private List<ChildrenBean> children;

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    public List<CheckArrBean> getCheckArr() {
        return checkArr;
    }

    public void setCheckArr(List<CheckArrBean> checkArr) {
        this.checkArr = checkArr;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public static class CheckArrBean {
        /**
         * type : 0
         * isChecked : 0
         */

        private String type;
        private String isChecked;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIsChecked() {
            return isChecked;
        }

        public void setIsChecked(String isChecked) {
            this.isChecked = isChecked;
        }
    }

    public static class ChildrenBean {
        /**
         * treeId : 340c6cb89dd3466980d80c8544db595f
         * title : 检查控制阀门的启闭状态
         * parentId : 1
         * spread : true
         * checkArr : [{"type":"0","isChecked":"0"}]
         * children : []
         */

        private String treeId;
        private String title;
        private String parentId;
        private boolean spread;
        private List<CheckArrBeanX> checkArr;
        private List<?> children;

        public String getTreeId() {
            return treeId;
        }

        public void setTreeId(String treeId) {
            this.treeId = treeId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public boolean isSpread() {
            return spread;
        }

        public void setSpread(boolean spread) {
            this.spread = spread;
        }

        public List<CheckArrBeanX> getCheckArr() {
            return checkArr;
        }

        public void setCheckArr(List<CheckArrBeanX> checkArr) {
            this.checkArr = checkArr;
        }

        public List<?> getChildren() {
            return children;
        }

        public void setChildren(List<?> children) {
            this.children = children;
        }

        public static class CheckArrBeanX {
            /**
             * type : 0
             * isChecked : 0
             */

            private String type;
            private String isChecked;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getIsChecked() {
                return isChecked;
            }

            public void setIsChecked(String isChecked) {
                this.isChecked = isChecked;
            }
        }
    }
}
