'use strict';

var flag = true;
var getName;

var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random).toString();
};

var theMess = function(text, str, done) {
	return {
                name:str,
		description:text,
		done: !!done,
		id: uniqueId()
	};
};

var appState = {
	mainUrl : 'WebChatApplication',
	messList:[],
	token : 'TN11EN'
};

function ref() {
        var username = document.getElementById('user').value;
        if(username==='') {
            alert('enter login');
            return;
        }
        var m = getName;
        getName = username;
        document.getElementById('user').value = '';
        if(flag) {
            flag = false;
            run();
        }
        var messList = appState.messList; 
        for(var i = 0; i < messList.length; i++) {
            if(messList[i].name !== m)
                continue;
            messList[i].name = getName;
            put(appState.mainUrl + '?id=' + messList[i].id, JSON.stringify(messList[i]), function() {
            });
        }
    }

function run() {
	var appContainer = document.getElementsByClassName('page')[0];

	appContainer.addEventListener('click', delegateEvent);
        appContainer.addEventListener('keydown', delegateEvent);
	setInterval(restore,500);
}

function createAllMessages(allMessages) {
        appState.messList = [];
        var items = document.getElementsByClassName('chat vis')[0];
        while (items.firstChild) {
            items.removeChild(items.firstChild);
        }
	for(var i = 0; i < allMessages.length; i++){
		addTodoInternal(allMessages[i]);
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
	var newMess = theMess(todoText.value, getName, false);

	if(todoText.value === '')
		return;

	todoText.value = '';
	addTodo(newMess, function() {
	});
} 

function enter(evtObj) {
        if(evtObj.keyCode !== 13)
            return;
	var messList = appState.messList; 
        var k = evtObj.target.parentNode;
        var text = evtObj.target.value;
        k.removeChild(evtObj.target);
        k.appendChild(document.createTextNode(text));
        for(var i = 0; i < messList.length; i++) {
            if(messList[i].id !== k.id)
                continue;
            messList[i].description = evtObj.target.value;
            put(appState.mainUrl + '?id=' + messList[i].id, JSON.stringify(messList[i]), function() {
            });
            return;
        }    
}

function onCngItem(divItem) {
	var messList = appState.messList;  
        for(var i = 0; i < messList.length; i++) {
            if(messList[i].id !== divItem.id)
                continue;

            if(messList[i].done || getName !== messList[i].name)
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
	var messList = appState.messList;

	for(var i = 0; i < messList.length; i++) {
		if(messList[i].id !== divItem.id)
			continue;
                    
                if(getName !== messList[i].name)
                    return;
                
		toggle(messList[i], function() {
		});

		return;
	}
}

function toggle(mess, continueWith) {
	mess.done = !mess.done;
	del(appState.mainUrl + '?id=' + mess.id, JSON.stringify(mess), function() {
	});
}

function addTodo(mess, continueWith) {
	post(appState.mainUrl, JSON.stringify(mess), function(){
	});
}

function addTodoInternal(mess) {
        var item = createItem(mess);
	var items = document.getElementsByClassName('chat vis')[0];
        var messList = appState.messList;
        	
        messList.push(mess);
	items.appendChild(item);
}

function createItem(mess) {
	var divItem = document.createElement('div');
	divItem.classList.add('item');
        divItem.id = mess.id;
        
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
        
        divItem.appendChild(document.createTextNode(' '+mess.name+': '));
        divItem.appendChild(document.createTextNode(mess.description));
        if(mess.done) {
            divItem.classList.add('deleted');
            divItem.childNodes[1].textContent = 'res';
        }
	return divItem;
}

function restore(continueWith) {
	var url = appState.mainUrl + '?token=TN11EN' + '&name=' + getName;

	get(url, function(responseText) {
		console.assert(responseText !== null);

		var response = JSON.parse(responseText);

		appState.token = response.token;
                
                createAllMessages(response.messages);

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
	var xhr = new XMLHttpRequest();

	continueWithError = continueWithError || defaultErrorHandler;
	xhr.open(method || 'GET', url, true);

	xhr.onload = function () {
		if (xhr.readyState !== 4)
			return;

		if(xhr.status !== 200 && xhr.status !== 304) {
			location.href='errjsp.jsp?er='+xhr.status;
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
    	'- server is active\n');

        continueWithError();
    };

    xhr.send(data);
}