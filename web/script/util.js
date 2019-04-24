function findParentElem(element, targetParent) {
    while(element.parentNode) {
        element = element.parentNode;
        
        //To stop the loop for reaching the unexisting parent
        // causing undefined errors
        if(element.tagName == null){
            break;
        }
        
        var isFound = element.classList.contains(targetParent);
        
//        console.log('isFound?', isFound, 'element->', element);
        if (isFound) {
//            console.log('target element found!');
            return element;
        }
    }
    
    return null;
}

function toTwoDecimal(num) {
    return parseFloat(num).toFixed(2);
}

function filterInput(input, regex) {
    return regex.test(input);
}

function capitalize(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function removeElemChild(elem) {
    while(elem.lastChild) {
        elem.removeChild(elem.lastChild);
    }
    
    return elem;
}

/**
 * Adds option into the selectbox with data of (id & name)
 * @param {type} elem
 * @param {type} data
 * @return {void}
 */
function addOption(elem, data) {
    
    data.forEach(function(item) {
        var option = document.createElement('option');
        option.innerHTML = capitalize(item.name).replace('-', ' ');
        option.value = item.id;
        
        elem.appendChild(option);
    });
}

/**
 * Just adds an option filler to a select box
 * @param {type} elem
 * @param {type} text
 * @return {void}
 */
function addOptionFiller(elem, text) {
    var option = document.createElement('option');
    option.value = "";
    option.innerHTML = text;
    option.disabled = true;
    option.style.display = 'none';
    option.selected = true;
    
    elem.appendChild(option);
}

function sendRequest(url, params, request) {
    var request = new XMLHttpRequest();
    
    if(request.toUpperCase() === 'GET') {
        request.open(request, url + params, true);
        request.onreadystatechange = function() {
            if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                return this.responseText;
            }else {
                return 'Error encountered!';
            }
        }
        request.send();
    }else if (request.toUpperCase() === 'POST') {
        request.open(request, url, true);
        request.setRequestHeader('application/x-www-form-urlencoded');
        request.onreadystatechange = function() {
            if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                return this.responseText;
            }else {
                return 'Error encountered!';
            }
        }
        request.send(params);
    }
    
    return null;
}