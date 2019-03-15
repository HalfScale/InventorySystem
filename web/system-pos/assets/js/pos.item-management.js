
var itemRow = document.querySelector('.pos-table-body');
var settingModal = document.querySelector('#pos-quantity-modal');
var setModalOkBttn = document.querySelector('.pos-modal-ok-button');
var setModalCancelBttn = document.querySelector('.pos-modal-cancel-button');

var modalQtyInput = document.querySelector('.pos-modal-setting-input');
var cartItems = document.querySelector('#cart-item-list');
var checkoutBttn = document.querySelector("#checkout-button");

var currentItemMap;
var itemTotalAmount = 0;
checkoutBttn.disabled = true;

setModalOkBttn.onclick = function() {
    if(!filterInput(modalQtyInput.value, /^[1-9]\d*$/)){
        alert('Invalid input!');
        return;
    }
    
    if(currentItemMap['stock'] >= modalQtyInput.value) {
        currentItemMap['cartQuantity'] = modalQtyInput.value;
    //    console.log('currentItemMap', currentItemMap);
        var itemBox = createCartItemBox(currentItemMap);

        if (isDuplicate(cartItems, itemBox.dataset['itemId'])) {
            console.log('this item id' + itemBox.dataset['itemId'] + ' is duplicate');
            alert('Duplicate items are not allowed');
            return false;
        }
    }else {
        alert('Insufficient item stock!');
    }
    
    itemTotalAmount += parseFloat(itemBox.dataset['itemSubtotal']);
    cartItems.appendChild(itemBox);
    checkoutBttn.innerHTML = 'Checkout -> &#8369;' + toTwoDecimal(itemTotalAmount);
    checkoutBttn.disabled = false;
    
    settingModal.style.display = 'none';
};

setModalCancelBttn.onclick = function() {
    settingModal.style.display = 'none';
};

cartItems.addEventListener('click', function(event) {
   if(!event.target.matches('.item-remove-bttn')) return;
   
   var sibling = event.target.previousElementSibling;
   var totalAmount = sibling.getAttribute('title');
   var divBox = findParentElem(event.target, 'cart-item-box');
   cartItems.removeChild(divBox);
   
   itemTotalAmount -= totalAmount;
   if (itemTotalAmount == 0) {
       checkoutBttn.disabled = true;
   }
   checkoutBttn.innerHTML = 'Checkout -> &#8369;' + toTwoDecimal(itemTotalAmount);
});

itemRow.addEventListener('click', function(event) {
    console.log(event.target.parentNode);
    modalQtyInput.value = 1;
    
    var row = event.target.parentNode;
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var itemId = row.dataset.itemId;
    console.log('itemId',itemId);
    var param = '?itemId=' + itemId + '&command=LOAD';

    xmlhttp.open('GET', url + param, true);

    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log('query result', this.responseText);
            currentItemMap = JSON.parse(this.responseText);
            settingModal.style.display = 'block';
            modalQtyInput.focus();
        }
    }
    
    xmlhttp.send();
});

function createCartItemBox(data) {
    var subTotal = data.cartQuantity * data.price;
    var container = document.createElement('div');
    container.className += ' cart-item-box';
    container.dataset['itemId'] = data.id;
    container.dataset['itemSubtotal'] = subTotal;
    // for displaying checkout total amount
    
    // sections of div box (3 sections)
    var firstSection = document.createElement('section');
    var secondSection = document.createElement('section');
    var thirdSection = document.createElement('section');
    
    var itemName = document.createElement('span');
    itemName.innerHTML = data.name;
    var itemCode = document.createElement('span');
    itemCode.innerHTML = data.code;
    var itemPrice = document.createElement('span');
    itemPrice.innerHTML = 'Price: &#8369; ' + toTwoDecimal(data.price);
    var itemQuantity = document.createElement('span');
    itemQuantity.innerHTML = 'Qty: ' + data.cartQuantity;
    var itemTotal = document.createElement('span');
    itemTotal.innerHTML = 'Total price: &#8369; '+ toTwoDecimal(subTotal);
    itemTotal.setAttribute('title', toTwoDecimal(subTotal));
    var removeBttn = document.createElement('u');
    removeBttn.innerHTML = 'Remove';
    removeBttn.className += 'item-remove-bttn clicky';
    
    firstSection.appendChild(itemName);
    firstSection.appendChild(itemCode);
    
    secondSection.appendChild(itemPrice);
    secondSection.appendChild(itemQuantity);
    
    thirdSection.appendChild(itemTotal);
    thirdSection.appendChild(removeBttn);
    
    container.appendChild(firstSection);
    container.appendChild(secondSection);
    container.appendChild(thirdSection);
    
    return container;
}

function isDuplicate(parentElem, targetId) {
    var children = parentElem.children;
    
    for(var i = 0; i < children.length; i++) {
        var elemId = parentElem.children[i];
        if (elemId.dataset['itemId'] === targetId) {
            console.log('isDuplicate');
            return true;
        }
    }
    
    return false;
}
