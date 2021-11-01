package com.ancely.fyw.mvptext;

public interface TestContract {
    interface Model {
        void execudeTest(String accound, String psw) throws Exception;

        void execudeTest1(String accound, String psw) throws Exception;
    }

    interface View {
        void handlerResult(TestInfo t);

        void handlerResult1(TestOneInfo oneInfo);
    }

    interface Persenter {
        void requestTest(String accound, String psw);
        void requestTest1(String accound, String psw);
        void requestSuccess(TestInfo testInfo);
    }
}