public class CWE_676_DangerousFunction {
    public void stopWorker() {
        Thread t1 = new Thread(new WorkerTask());
        // 정규식은 'thread'나 'worker'라는 단어만 보므로, 't1.stop()'은 우회될 수 있음
        t1.stop(); // CWE-676 취약점 발생
    }
}
