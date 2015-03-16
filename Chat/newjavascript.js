function run(){
	var appContainer = document.getElementsByClassName('page')[0];
	appContainer.addEventListener('click', delegateEvent);
        appContainer.addEventListener('keydown', delegateEvent);
}


function delegateEvent(evtObj) {
    if(evtObj.type === 'click' && evtObj.target.classList.contains('send')) {
	onAddButtonClick(evtObj);
    }
    if(evtObj.type === 'click' && evtObj.target.classList.contains('buttons')) {
        onCngAreaClick(evtObj.target);
    }
    if(evtObj.type === 'keydown' && evtObj.target.classList.contains('vis')) {
        enter(evtObj);
    }
    if(evtObj.type === 'click' && evtObj.target.classList.contains('buttons1')) {
	var cng = evtObj.target.parentElement;
        alert(evtObj.target.id === '1');
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
}
function onCngAreaClick(vr) {
    var n = vr.parentNode;
    var i = 0;
    while(n.children[i]!==vr)
    {i++;}
    var items = document.getElementsByClassName('chat');
    for(var j = 0; j < items.length; j++) {
        if(j===i){
            items[j].classList.remove('hid');
            items[j].classList.add('vis');
        }
        else {
            items[j].classList.remove('vis');
            items[j].classList.add('hid');
        }
    }
}
function onDelItem(cng) {
    var all = cng.parentNode;
    all.removeChild(cng);
}
function onCngItem(cng) {
    var all = document.createElement('input');
    all.setAttribute('type','text');
    all.classList.add('vis');
    all.value = cng.childNodes[3].textContent;
    cng.removeChild(cng.childNodes[3]);
    cng.appendChild(all);
    //alert(cng.childNodes[2]);
}
function onAddButtonClick(){
	var todoText = document.getElementById('message');

	addTodo(todoText.value);
        todoText.value = "";
} 

function addTodo(value) {
	if(!value){
		return;
	}

	var item = createItem(value);
	var items = document.getElementsByClassName('chat vis')[0];

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
        
        divItem.appendChild(document.createTextNode(' user: '));
        divItem.appendChild(document.createTextNode(text));

	return divItem;
}