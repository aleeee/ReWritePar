package starter;

public class JTest {
	
	
//	final ExecutorService executor = Executors.newFixedThreadPool(numWorkerThreads);
//	final LinkedBlockingQueue<Future> futures = new LinkedBlockingQueue<>(maxQueueSize);
//	try {   
//	    Thread taskGenerator = new Thread() {
//	        @Override
//	        public void run() {
//	            while (reader.hasNext) {
//	                Callable task = generateTask(reader.next());
//	                Future future = executor.submit(task);
//	                try {
//	                    // if queue is full blocks until a task
//	                    // is completed and hence no future tasks are submitted.
//	                    futures.put(compoundFuture);
//	                } catch (InterruptedException ex) {
//	                    Thread.currentThread().interrupt();         
//	                }
//	            }
//	        executor.shutdown();
//	        }
//	    }
//	    taskGenerator.start();
//
//	    // read from queue as long as task are being generated
//	    // or while Queue has elements in it
//	    while (taskGenerator.isAlive()
//	                    || !futures.isEmpty()) {
//	        Future compoundFuture = futures.take();
//	        // do something
//	    }
//	} catch (InterruptedException ex) {
//	    Thread.currentThread().interrupt();     
//	} catch (ExecutionException ex) {
//	    throw new MyException(ex);
//	} finally {
//	    executor.shutdownNow();
//	}
}
