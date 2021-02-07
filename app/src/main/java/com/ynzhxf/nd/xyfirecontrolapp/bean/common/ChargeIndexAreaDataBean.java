package com.ynzhxf.nd.xyfirecontrolapp.bean.common;


import java.util.List;

/**
 * author hbzhou
 * date 2019/5/30 16:44
 */
public class ChargeIndexAreaDataBean {

    /**
     * ID : 1
     * Name : 云南省
     * NodeLevel : 1
     * ParentID : null
     * ChildrenNodes : [{"ID":"2","Name":"昆明市","NodeLevel":2,"ParentID":"1","ChildrenNodes":[{"ID":"3901b60e7f944123824c883da5166423","Name":"官渡区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]},{"ID":"63abc9762e0d43048f01fe292f179872","Name":"盘龙区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]},{"ID":"9398d8898b1c4bc2b69470f65845ef59","Name":"五华区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]},{"ID":"c8be0932cba84d08b294f3d13821a345","Name":"西山区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]},{"ID":"d7834bfcf5a44fb1ba63fbe661b1c24e","Name":"呈贡区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]}]},{"ID":"3","Name":"曲靖市","NodeLevel":2,"ParentID":"1","ChildrenNodes":[{"ID":"05885d0b934e4f678753c8df33f060b6","Name":"沾益县","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]},{"ID":"0738a8ee0741427cb5fddbd161b7b666","Name":"会泽县","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]},{"ID":"267c9cdfc0414d0fb164ef057750cbcd","Name":"罗平县","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]},{"ID":"31417047ae0f4783bae4d60a717e9da0","Name":"麒麟区","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]},{"ID":"3bb2084d15564a5eb7660426070b706a","Name":"宣威市","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]},{"ID":"54ce53a99873474f869647f7d115b78a","Name":"陆良县","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]},{"ID":"6ea9f43e4a8f4c849462c2afa1555759","Name":"经开区","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]},{"ID":"b5d5e3de03dc4cc9a83bdd2a64344348","Name":"马龙县","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]},{"ID":"be0156d3360140c6aca1996a36e2eb6c","Name":"师宗县","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]},{"ID":"f8299160647e48f8865388052bb36032","Name":"富源县","NodeLevel":3,"ParentID":"3","ChildrenNodes":[]}]},{"ID":"ff746eca8a204886acaa10a0a899abf1","Name":"楚雄州","NodeLevel":2,"ParentID":"1","ChildrenNodes":[]}]
     */

    private String ID;
    private String Name;
    private int NodeLevel;
    private Object ParentID;
    private List<ChildrenNodesBeanX> ChildrenNodes;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getNodeLevel() {
        return NodeLevel;
    }

    public void setNodeLevel(int NodeLevel) {
        this.NodeLevel = NodeLevel;
    }

    public Object getParentID() {
        return ParentID;
    }

    public void setParentID(Object ParentID) {
        this.ParentID = ParentID;
    }

    public List<ChildrenNodesBeanX> getChildrenNodes() {
        return ChildrenNodes;
    }

    public void setChildrenNodes(List<ChildrenNodesBeanX> ChildrenNodes) {
        this.ChildrenNodes = ChildrenNodes;
    }

    public static class ChildrenNodesBeanX {
        /**
         * ID : 2
         * Name : 昆明市
         * NodeLevel : 2
         * ParentID : 1
         * ChildrenNodes : [{"ID":"3901b60e7f944123824c883da5166423","Name":"官渡区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]},{"ID":"63abc9762e0d43048f01fe292f179872","Name":"盘龙区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]},{"ID":"9398d8898b1c4bc2b69470f65845ef59","Name":"五华区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]},{"ID":"c8be0932cba84d08b294f3d13821a345","Name":"西山区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]},{"ID":"d7834bfcf5a44fb1ba63fbe661b1c24e","Name":"呈贡区","NodeLevel":3,"ParentID":"2","ChildrenNodes":[]}]
         */

        private String ID;
        private String Name;
        private int NodeLevel;
        private String ParentID;
        private List<ChildrenNodesBean> ChildrenNodes;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getNodeLevel() {
            return NodeLevel;
        }

        public void setNodeLevel(int NodeLevel) {
            this.NodeLevel = NodeLevel;
        }

        public String getParentID() {
            return ParentID;
        }

        public void setParentID(String ParentID) {
            this.ParentID = ParentID;
        }

        public List<ChildrenNodesBean> getChildrenNodes() {
            return ChildrenNodes;
        }

        public void setChildrenNodes(List<ChildrenNodesBean> ChildrenNodes) {
            this.ChildrenNodes = ChildrenNodes;
        }

        public static class ChildrenNodesBean {
            /**
             * ID : 3901b60e7f944123824c883da5166423
             * Name : 官渡区
             * NodeLevel : 3
             * ParentID : 2
             * ChildrenNodes : []
             */

            private String ID;
            private String Name;
            private int NodeLevel;
            private String ParentID;
            private List<?> ChildrenNodes;

            public String getID() {
                return ID;
            }

            public void setID(String ID) {
                this.ID = ID;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public int getNodeLevel() {
                return NodeLevel;
            }

            public void setNodeLevel(int NodeLevel) {
                this.NodeLevel = NodeLevel;
            }

            public String getParentID() {
                return ParentID;
            }

            public void setParentID(String ParentID) {
                this.ParentID = ParentID;
            }

            public List<?> getChildrenNodes() {
                return ChildrenNodes;
            }

            public void setChildrenNodes(List<?> ChildrenNodes) {
                this.ChildrenNodes = ChildrenNodes;
            }
        }
    }
}
