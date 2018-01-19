# VideoGen2
Yet another variant of a configurable generator of generator of video variants

# Students work  
Cédric et Victor  
  
Our work is in the folder DelourmeDemeautis.  
  
Some tests functions are available in /src/main/java/main/model.   

Web application:  
	Serveur : Spring boot  
	Client : AngularJS  
To launch the client : http://localhost:9200  

Two web pages:  
	1/ generate a variant of a videogen file  
	2/ generate a custom video from a videogen file  
  
Managed requests :  
	GET /generate : give to a client a generated variant of a videogen.  
	GET /custom : give to a client a "reduced" videogen with pictures to simplify the customize session.  
	POST /generatedVOD : collect from a client, a list of videos to concatenate.  
	GET / generatedVOD : give to a client a generated customized video.  
  
All videos are in the folder /video.  
