******************
NSD Messaging App
READ ME
******************

Files--------------------------------   
MainClient.java----------- Client side application(Sends requests and receives responses from server)   
MainServer.java----------- Server Side/ Contains Client Handler (Sends response and receives requests from clients)(Stores and reads from disk)         
Message.java-------------- JSON format: Serializes and deserialize message      
Request.java-------------- JSON format: Super-class for all requests    
RequestGet.java----------- JSON format: Serializes and deserialize Get requests         
RequestOpen.java---------- JSON format: Serializes and deserialize Open requests        
RequestPublish.java------- JSON format: Serializes and deserialize Publish requests     
RequestQuit.java---------- JSON format: Serializes and deserialize Quit requests        
RequestSearch.java-------- JSON format: Serializes and deserialize Search requests      
RequestSubscribe.java----- JSON format: Serializes and deserialize Subscribe requests   
RequestUnsubscribe.java--- JSON format: Serializes and deserialize Unsubscribe requests         
Response.java------------- JSON format: Super-class for all responses   
ResponseError.java-------- JSON format: Serializes and deserialize message response     
ResponseMessageList.java-- JSON format: Serializes and deserialize message response     
ResponseSuccess.java------ JSON format: Serializes and deserialize Success response     
MessageHistory.txt.java--- Storage for all messages     
Users.txt.java------------ Storage for all users        
        
Extra--------------------------------   
persistence - In the main server is stores any messages sent into a txt file Also when user logs in Chat history loads in from disk     
Chat searching - Search function allow to search word or sentence within subscribed channel history     
        
How_To_Run--------------------------- 
1. Open huh Project in intellij 
2. implment the json-simple-1.1.1 libary to project     
3. change file path location (MainServer: line 48, 59, 73, 87) to fit your desktop file path    
4. Make sure port 12345 is free, if not change port number      
5. Run MainServer first 
6. Run ClientServer after (you can run many instance of this file)      
        
Commands-----------------------------   
Login-------/log<username>-------       
Subscribe---/sub<ChannelName>----       
Message-----/say<Message>--------       
ReadAll-----/read<Timestamp>-----       
Unsubscribe-/unsub<ChannelName>--       
Search------/search<ChannelName>-       
Exit--------/quit----------------       
Commands----/com-----------------       
        
-------------------------------------   
Thanks For Reading!     
