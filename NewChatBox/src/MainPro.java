
public class MainPro {
	public static void main (String args[]){
		ChattingThread th1 = new ChattingThread ("mm");	
		th1.start();


	}
	
	public static class ChattingThread extends Thread{
		String name ;
		public ChattingThread (String name){
			this.name = name;
		}
		
		public void run (){
			UI u = new UI (name);
			u.polling();
		}
	}
}
