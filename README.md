# README #

To run app:
<b>mvn spring-boot:run</b>

To create task:
send post reqest to: http://localhost:8080/task
with body:
```
{
	"name" : "some task",
	"priceAmount" : 33.22,
	"statusFlag" : "OPENED",
	"category" : {
		"name" : "Computers"
	},
	"address" : "tttt",
	"customer" : {
		"person" : {
			"firstname" : "nikita",
			"city": {
				"name": "samara"
			},
			"phone": "6666",
			"email": "nsalomatin@hotmail.com"
		}
	}
}
```

to get all tasks:
send get request to: http://localhost:8080/tasks?status=ANY&category=ALL
