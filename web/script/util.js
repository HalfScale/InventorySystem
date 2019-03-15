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