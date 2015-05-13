'use strict';

var intervalID;

function getName() {
    var params = location.search;
    return params.split("=")[1];
}

var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random).toString();
};

var theTask = function(text, str, done) {
	return {
                name:str,
		description:text,
		done: !!done,
		id: uniqueId()
	};
};

var appState = {
	mainUrl : 'WebChatApplication',
	taskList:[],
	token : 'TN11EN'
};

function run() {
	var appContainer = document.getElementsByClassName('page')[0];

	appContainer.addEventListener('click', delegateEvent);
        appContainer.addEventListener('keydown', delegateEvent);
	restore();
}

function createAllTasks(allTasks) {
        appState.taskList = [];
        var items = document.getElementsByClassName('chat vis')[0];
        while (items.firstChild) {
            items.removeChild(items.firstChild);
        }
	for(var i = 0; i < allTasks.length; i++){
		addTodoInternal(allTasks[i]);
            }
}

function delegateEvent(evtObj) {
	if(evtObj.type === 'click' && evtObj.target.classList.contains('send')) {
            onAddButtonClick(evtObj);
        }
        if(evtObj.type === 'keydown' && evtObj.target.classList.contains('vis')) {
            enter(evtObj);
        }
        if(evtObj.type === 'click' && evtObj.target.classList.contains('buttons1')) {
            var cng = evtObj.target.parentElement;
            if(evtObj.target.id === '1') {
                onCngItem(cng);
            }
            else
                onDelItem(cng);
        }
}

function onAddButtonClick(){
	var todoText = document.getElementById('message');
	var newTask = theTask(todoText.value, getName(), false);

	if(todoText.value === '')
		return;

	todoText.value = '';
	addTodo(newTask, function() {
	});
} 

function enter(evtObj) {
        if(evtObj.keyCode !== 13)
            return;
	var taskList = appState.taskList; 
        var k = evtObj.target.parentNode;
        var text = evtObj.target.value;
        k.removeChild(evtObj.target);
        k.appendChild(document.createTextNode(text));
        for(var i = 0; i < taskList.length; i++) {
            if(taskList[i].id !== k.id)
                continue;
            taskList[i].description = evtObj.target.value;
            put(appState.mainUrl + '?id=' + taskList[i].id, JSON.stringify(taskList[i]), function() {
            });
            return;
        }    
}

function onCngItem(divItem) {
	var taskList = appState.taskList;  
        for(var i = 0; i < taskList.length; i++) {
            if(taskList[i].id !== divItem.id)
                continue;

            if(taskList[i].done || getName() !== taskList[i].name)
                return;
            
            var all = document.createElement('input');
            all.classList.add('vis');
            all.setAttribute('type','text');
            all.value = divItem.childNodes[3].textContent;
            divItem.removeChild(divItem.childNodes[3]);
            divItem.appendChild(all);
            return;
        }
}

function onDelItem(divItem) {
	var taskList = appState.taskList;

	for(var i = 0; i < taskList.length; i++) {
		if(taskList[i].id !== divItem.id)
			continue;
                    
                if(getName() !== taskList[i].name)
                    return;
                
		toggle(taskList[i], function() {
		});

		return;
	}
}

function toggle(task, continueWith) {
	task.done = !task.done;
	del(appState.mainUrl + '?id=' + task.id, JSON.stringify(task), function() {
	});
}

function addTodo(task, continueWith) {
	post(appState.mainUrl, JSON.stringify(task), function(){
	});
}

function addTodoInternal(task) {
        var item = createItem(task);
	var items = document.getElementsByClassName('chat vis')[0];
        var taskList = appState.taskList;
        	
        taskList.push(task);
	items.appendChild(item);
}

function createItem(task) {
	var divItem = document.createElement('div');
	divItem.classList.add('item');
        divItem.id = task.id;
        
	var btn = document.createElement('button');
        btn.classList.add('buttons1');
	btn.textContent = "edit";
        btn.id = 1;
	divItem.appendChild(btn);
        
        btn = document.createElement('button');
        btn.classList.add('buttons1');
	btn.textContent = "del";
        btn.id = 2;
	divItem.appendChild(btn);
        
        divItem.appendChild(document.createTextNode(' '+task.name+': '));
        divItem.appendChild(document.createTextNode(task.description));
        if(task.done) {
            divItem.classList.add('deleted');
            divItem.childNodes[1].textContent = 'res';
        }
	return divItem;
}

function restore(continueWith) {
	var url = appState.mainUrl + '?token=TN11EN' + '&name=' + getName();

	get(url, function(responseText) {
		console.assert(responseText !== null);

		var response = JSON.parse(responseText);

		appState.token = response.token;
		createAllTasks(response.tasks);

		continueWith && continueWith();
	});
}

function defaultErrorHandler(message) {
	console.error(message);
}

function get(url, continueWith, continueWithError) {
	ajax('GET', url, null, continueWith, continueWithError);
}

function post(url, data, continueWith, continueWithError) {
	ajax('POST', url, data, continueWith, continueWithError);	
}

function put(url, data, continueWith, continueWithError) {
	ajax('PUT', url, data, continueWith, continueWithError);	
}

function del(url, data, continueWith, continueWithError) {
	ajax('DELETE', url, data, continueWith, continueWithError);	
}

function isError(text) {
	if(text === "")
		return false;
	
	try {
		var obj = JSON.parse(text);
	} catch(ex) {
		return true;
	}

	return !!obj.error;
}

function ajax(method, url, data, continueWith, continueWithError) {
    alert(method);
	var xhr = new XMLHttpRequest();

	continueWithError = continueWithError || defaultErrorHandler;
	xhr.open(method || 'GET', url, true);

	xhr.onload = function () {
		if (xhr.readyState !== 4)
			return;

		if(xhr.status !== 200) {
			continueWithError('Error on the server side, response ' + xhr.status);
			return;
		}

		if(isError(xhr.responseText)) {
			continueWithError('Error on the server side, response ' + xhr.responseText);
			return;
		}

		continueWith(xhr.responseText);
	};    

    xhr.ontimeout = function () {
    	ontinueWithError('Server timed out !');
    };

    xhr.onerror = function (e) {
    	alert('Server connection error !\n'+
    	'\n' +
    	'Check if \n'+
    	'- server is active\n'+
    	'- server sends header "Access-Control-Allow-Origin:*"');

        continueWithError();
    };

    xhr.send(data);
}
