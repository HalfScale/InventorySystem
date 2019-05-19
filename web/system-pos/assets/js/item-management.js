
/* global parseFloat */

var itemTable = document.querySelector('.pos-table-body');
var settingModal = document.querySelector('#pos-quantity-modal');
var setModalOkBttn = document.querySelector('.pos-modal-ok-button');
var setModalCancelBttn = document.querySelector('.pos-modal-cancel-button');
var checkoutModal = document.querySelector("#checkout-modal");

var modalQtyInput = document.querySelector('.pos-modal-setting-input');
var cartItems = document.querySelector('#cart-item-list');
var modalTypeInput = document.querySelector('.pos-modal-price-type');

var checkoutBttn = document.querySelector("#checkout-button");
var checkoutCancelBttn = document.querySelector(".checkout-cancel");
var checkoutConfirmBttn = document.querySelector(".checkout-confirm");

var transactionTypeSelect = document.querySelector(".transaction-type-select");

var currentItemMap;
var itemTotalAmount = 0;
//initialize checkout button
checkoutBttn.setAttribute('title', 0);
checkoutBttn.disabled = true;

getTransactionTypes();

setModalOkBttn.onclick = function() {
    var itemRow = itemTable.children;
    console.log('itemRow', itemRow);
    
    if(!filterInput(modalQtyInput.value, /^[1-9]\d*$/)){
        alert('Invalid input!');
        return;
    }
    
    if(currentItemMap['stock'] >= modalQtyInput.value) {
        var quantity = modalQtyInput.value;
        var stock = currentItemMap['stock'];
        currentItemMap['cartQuantity'] = quantity;
        var itemBox = createCartItemBox(currentItemMap);
        console.log('currentItemMap', currentItemMap);
        
        if (isDuplicate(cartItems, itemBox.dataset['itemId'])) {
            console.log('this item id' + itemBox.dataset['itemId'] + ' is duplicate');
            alert('Duplicate items are not allowed');
            return false;
        }
        
        for(var i = 0; i < itemRow.length; i++){
            if (itemRow[i].dataset['itemId'] == currentItemMap['id'] ) {
                console.log('children 7', itemRow[i].children[7].innerHTML);
                itemRow[i].children[7].innerHTML = stock - quantity;
                break;
            }
        }

    }else {
        alert('Insufficient item stock!');
    }
    
    itemTotalAmount += parseFloat(itemBox.dataset['itemSubtotal']);
    cartItems.appendChild(itemBox);
    checkoutBttn.setAttribute('title', itemTotalAmount);
    checkoutBttn.innerHTML = 'Checkout -> &#8369;' + toTwoDecimal(itemTotalAmount);
    checkoutBttn.disabled = false;
    
    settingModal.style.display = 'none';
};

setModalCancelBttn.onclick = function() {
    settingModal.style.display = 'none';
};
var checkoutMap = {
    type: '',
    'checkoutItem[]': []
};
var checkoutItem = [];
checkoutBttn.onclick = function() {
    console.log('cart items', cartItems.children);
    var children = cartItems.children;
    for(var i = 0; i < children.length; i++) {
        var itemMap = {};
        
        var itemQuantity = children[i].querySelector('.item-quantity');
        var itemTotal = children[i].dataset['itemSubtotal'];
        var itemId = children[i].dataset['itemId'];
        console.log('item qty', itemQuantity.getAttribute('title'));
        itemMap['quantity'] = itemQuantity.getAttribute('title');
        itemMap['total'] = itemTotal;
        itemMap['id'] = itemId;
        
        checkoutItem.push(itemMap);
    }
    
    var checkoutTotal = checkoutModal.querySelector('.checkout-total');
    var total = checkoutBttn.getAttribute('title');
    checkoutTotal.innerHTML = '&#8369;' + parseFloat(total).toFixed(2);
    checkoutModal.style.display = 'block';
}

checkoutCancelBttn.onclick = function() {
    checkoutModal.style.display = 'none';
}

checkoutConfirmBttn.onclick = function() {
    checkoutMap['type'] = transactionTypeSelect.value;
    checkoutMap['checkoutItem[]'] = checkoutItem;
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    console.log('checkout map', checkoutMap);
    console.log('value', );
    var param = 'command=CHECKOUT' + '&param=' + JSON.stringify(checkoutMap);
    xmlhttp.open('POST', url, true);
    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log('items sold!');
            checkoutModal.style.display = 'none';
            while(cartItems.firstChild) {
                cartItems.removeChild(cartItems.firstChild);
            }
            checkoutItem = [];
            alert('items checked out!');
        }
    };
    
    xmlhttp.send(param);
}

cartItems.addEventListener('click', function(event) {
   if(!event.target.matches('.item-remove-bttn')) return;
   
   //Removes the cart items
   var sibling = event.target.previousElementSibling;
   var totalAmount = sibling.getAttribute('title');
   var divBox = findParentElem(event.target, 'cart-item-box');
   cartItems.removeChild(divBox);
   
   itemTotalAmount -= totalAmount;
   checkoutBttn.setAttribute('title', itemTotalAmount);
   if (itemTotalAmount == 0) {
       checkoutBttn.disabled = true;
   }
   
   //Search from the item table the removed cart item id
   //Then gets its stock
   //Then add it to the cartItem quantity
   var itemRow = itemTable.children;
   for(var i = 0; i < itemRow.length; i++){
        if (itemRow[i].dataset['itemId'] == divBox.dataset['itemId']) {
            var cartItemQuantity = divBox.querySelector('.item-quantity').getAttribute('title');
            var itemStock = itemRow[i].children[7].innerHTML;
            itemRow[i].children[7].innerHTML = parseFloat(cartItemQuantity) + parseFloat(itemStock);
            break;
        }
    }
        
   checkoutBttn.innerHTML = 'Checkout -> &#8369;' + toTwoDecimal(itemTotalAmount);
});

itemTable.addEventListener('click', function(event) {
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

function getTransactionTypes() {
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?command=LIST_TRANSACTION_TYPE';
    xmlhttp.open('GET', url + param, true);
    
    xmlhttp.onreadystatechange = function() {
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var transactionTypes = JSON.parse(this.responseText);
            
            transactionTypes.forEach(function(transaction) {
                var option = document.createElement('option');
                option.innerHTML = transaction.name;
                option.value = transaction.id;
                transactionTypeSelect.appendChild(option);
            });
        }
        
        console.log('trans type', transactionTypeSelect.value);
    }
    
    xmlhttp.send();
}

function createCartItemBox(data) {
    var price;
    
    if (modalTypeInput.value === 'original') {
        console.log('select input', modalTypeInput.value);
        price = data.price;
    }else {
        price = data.resellerPrice;
    }
    
    var subTotal = data.cartQuantity * price;
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
    itemName.className += 'item-name';
    var itemCode = document.createElement('span');
    itemCode.innerHTML = data.code;
    itemCode.className += 'item-code';
    var itemPrice = document.createElement('span');
    itemPrice.innerHTML = 'Price: &#8369; ' + toTwoDecimal(price);
    itemPrice.className += 'item-price';
    var itemQuantity = document.createElement('span');
    itemQuantity.innerHTML = 'Qty: ' + data.cartQuantity;
    itemQuantity.setAttribute('title', data.cartQuantity);
    itemQuantity.className += 'item-quantity';
    var itemTotal = document.createElement('span');
    itemTotal.innerHTML = 'Total price: &#8369; '+ toTwoDecimal(subTotal);
    itemTotal.setAttribute('title', toTwoDecimal(subTotal));
    itemTotal.className += 'item-total';
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
