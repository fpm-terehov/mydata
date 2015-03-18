var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random).toString();
};

var Mess = function(text, done) {
	return {
		description:text,
		done: done,
		id: uniqueId()
	};
};

var MessList = [];
var temp;

function run(){
	var appContainer = document.getElementsByClassName('page')[0];
	appContainer.addEventListener('click', delegateEvent);
        appContainer.addEventListener('keydown', delegateEvent);
        
        var allMess = restore();
        createAllTasks(allMess);
}

function createAllTasks(allTasks) {
    if(allTasks===null)return;
    for(var i = 0; i < allTasks.length; i++){
        addTodo(allTasks[i]);            
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

function enter(evtObj) {
    if(evtObj.keyCode !== 13)
        return;
    var k = evtObj.target.parentNode;
    var text = evtObj.target.value;
    k.removeChild(evtObj.target);
    k.appendChild(document.createTextNode(text));
    for(var i = 0; i < MessList.length; i++) {
	if(MessList[i].description !== temp)
            continue;
        MessList[i].description = evtObj.target.value;
        store(MessList);
        return;
    }    
}

function onDelItem(cng) {
    for(var i = 0; i < MessList.length; i++) {
		if(MessList[i].description !== cng.childNodes[3].textContent)
			continue;

		toggle(MessList[i]);
		updateItem(cng, MessList[i]);
		store(MessList);

		return;
	}
    store(MessList);
}

function onCngItem(cng) {    
    for(var i = 0; i < MessList.length; i++) {
        if(MessList[i].description !== cng.childNodes[3].textContent)
            continue;
                
        var all = document.createElement('input');
        all.classList.add('vis');
        all.setAttribute('type','text');
        all.value = cng.childNodes[3].textContent;
        temp = all.value;
        cng.removeChild(cng.childNodes[3]);
        cng.appendChild(all);
	store(MessList);
	return;
    }
    store(MessList); 
}

function toggle(task) {
	task.done = !task.done;
}

function onAddButtonClick(){
	var todoText = document.getElementById('message');
        if(todoText === '')
		return;
        var newMess = Mess(todoText.value,false);
	addTodo(newMess);
        todoText.value = "";
	store(MessList);
} 

function addTodo(value) {
	var item = createItem(value);
	var items = document.getElementsByClassName('chat vis')[0];
        	
        MessList.push(value);
	items.appendChild(item);
}

function createItem(text){
	var divItem = document.createElement('div');
	divItem.classList.add('item');
        
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
        
        divItem.appendChild(document.createTextNode(' '+getName()+': '));
        divItem.appendChild(document.createTextNode(text.description));
        if(text.done) {
            divItem.classList.add('deleted');
            divItem.childNodes[1].textContent = 'res';
        }
	return divItem;
}

function getName() {
    var params = location.search;
    return params.split("=")[1];
}

function store(listToSave) {
	localStorage.setItem(getName(), JSON.stringify(listToSave));
}

function restore() {
	var item = localStorage.getItem(getName());

	return item && JSON.parse(item);
}

function updateItem(divItem, task){
    if(task.done) {
        divItem.classList.add('deleted');
        divItem.childNodes[1].textContent = 'res';
    } else {
        divItem.classList.remove('deleted');
        divItem.childNodes[1].textContent = 'del';
    }
}