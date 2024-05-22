console.log("hotel.js 호출되었음");

let hotel = (function () {

    
    //distchief/store/list의 총판 셀렉트박스 선택시
    function selectDistOfStore(selectedDist){

        $.ajax({
            type: 'GET',
            url: '/selectstore', // 서버에서 storeList를 반환하는 엔드포인트
            data: { distName: selectedDist},
            contentType : "application/json; charset=utf-8",

            success: function(data) {


                var distbrandOfStore = data.distbrandOfStore;
                var distOfBrand = data.distOfBrand;

                $('#brandSelect').empty();
                $('#brandSelect').append($('<option>').val('').text('전체'));
                $('#storeSelect').empty();
                $('#storeSelect').append($('<option>').val('').text('전체'));

                $.each(distOfBrand, function(index, distOfBrand) {
                    console.log(distOfBrand)
                    $('#brandSelect').append($('<option>').val(distOfBrand.brandName).text(distOfBrand.brandName));
                });

                $.each(distbrandOfStore, function(index, distbrandOfStore) {
                    console.log(distbrandOfStore)
                    $('#storeSelect').append($('<option>').val(distbrandOfStore.storeName).text(distbrandOfStore.storeName));
                });



            },
            error: function(xhr, status, error) {
                console.error('Failed to retrieve stores: ' + error);
            }
        });

    };



    //distchief/store/list의 브랜드 셀렉트박스 선택시
    function selectDistAndBrandOfStore(selectedBrand) {

        $.ajax({
            type: 'GET',
            url: '/selectstore',
            data: { brandName: selectedBrand},
            contentType : "application/json; charset=utf-8",

            success: function(data) {

                var distbrandOfStore = data.distbrandOfStore;

                $('#storeSelect').empty();
                $('#storeSelect').append($('<option>').val('').text('전체'));


                $.each(distbrandOfStore, function(index, distbrandOfStore) {
                    console.log(distbrandOfStore)
                    $('#storeSelect').append($('<option>').val(distbrandOfStore.storeName).text(distbrandOfStore.storeName));
                });

            },
            error: function(xhr, status, error) {
                console.error('Failed to retrieve stores: ' + error);
            }
        });

    };




    function registerDistOfStore(selectedDist){

        $.ajax({
            type: 'GET',
            url: '/registerstore', // 서버에서 storeList를 반환하는 엔드포인트
            data: { distName: selectedDist},
            contentType : "application/json; charset=utf-8",

            success: function(data) {

                var distOfManager = data.distOfManager;
                var distOfBrand = data.distOfBrand;

                $('#brandSelect').empty();
                $('#brandSelect').append($('<option>').val('').text('전체'));
                $('#managerSelect').empty();
                $('#managerSelect').append($('<option>').val('').text('전체'));

                $.each(distOfBrand, function(index, distOfBrand) {
                    console.log(distOfBrand)
                    $('#brandSelect').append($('<option>').val(distOfBrand.brandName).text(distOfBrand.brandName));
                });

                $.each(distOfManager, function(index, distOfManager) {
                    console.log(distOfManager)
                    $('#managerSelect').append($('<option>').val(distOfManager.managerName).text(distOfManager.managerName));
                });

            },
            error: function(xhr, status, error) {
                console.error('Failed to retrieve stores: ' + error);
            }
        });

    };


    function searchEmptyRoom(searchDTO){

        $.ajax({
            type: 'GET',
            url: '/emptyroom',
            dataType: 'json',
            data: searchDTO,

            success: function(response) {

                console.log("결과값전체 화긴@@ "+response);

                var emptyRoomResultTable = $('#emptyRoomResultTable');
                emptyRoomResultTable.empty(); // 기존 결과 제거

                const emptyRooms = response.emptyRoomTypes;
                const notEmptyRooms = response.notEmptyRoomTypes;

                notEmptyRooms.forEach(function (e){
                    console.log(e.roomType);
                })


                var peopleNumRow = `
                                            <tr id="peopleNumId">
                        <td class="col-2" style="text-align: center;">
                            투숙인원 :
                        </td>
                        <td>
                            <input type="text" name="peopleNum">
                        </td>
                    </tr>`;

                emptyRoomResultTable.append(peopleNumRow);

                //예약 가능 방 행 추가
                emptyRooms.forEach(function(item) {

                    var newRow = `
                        <tr>
                            <td>${item.roomType}</td>
                            <td>
                                <img src="https://gudgh9512.s3.ap-northeast-2.amazonaws.com/static%5c${item.roomMainimgName}" width="400" height="200">
                            </td>
                            <td>
                                가격 : ${item.roomPrice}
                            </td>
                            <td>
                                <button class="btn button reserve-btn" type="button" data-room-idx="${item.roomIdx}" data-room-type="${item.roomType}" data-room-price="${item.roomPrice}">예약하기</button>
                            </td>
                        </tr>`;
                    emptyRoomResultTable.append(newRow);
                });

                
                //예약불가 방 행 추가
                notEmptyRooms.forEach(function(item) {

                    var newRow = `
                        <tr>
                            <td>${item.roomType}</td>
                            <td>
                                <img src="https://gudgh9512.s3.ap-northeast-2.amazonaws.com/static%5c${item.roomMainimgName}" width="400" height="200" class="notEmptyRoom">
                            </td>
                            <td>
                                가격 : ${item.roomPrice}
                            </td>
                            <td>
                                 <span class="badge bg-info custom-badge">매진</span>
                            </td>
                        </tr>`;
                    emptyRoomResultTable.append(newRow);
                });



                //예약 클릭시 폼 제출
                document.querySelectorAll('.reserve-btn').forEach(item => {


                    item.addEventListener('click', event => {

                        const roomIdx = event.target.getAttribute('data-room-idx');
                        const roomType = event.target.getAttribute('data-room-type');
                        const roomPrice = event.target.getAttribute('data-room-price');


                        document.getElementById('selectedRoomIdx').value = roomIdx;
                        document.getElementById('selectedRoomOrderType').value = roomType;
                        document.getElementById('selectedRoomOrderPrice').value = roomPrice;
                        document.getElementById('reservationForm').submit();
                    });
                });



                //위에서 받은 날짜 값 결과제출용 날짜 변수에 넣음
                $('#reservationDateCheckinResult').val(searchDTO.reservationDateCheckin);
                $('#reservationDateCheckOutResult').val(searchDTO.reservationDateCheckout);

            },
            error: function(xhr, status, error) {
                console.error('에러발생');
            }
        });

    };





    function searchRoomTypeData(roomType,storeIdx){

        $.ajax({
            type: 'GET',
            url: '/roomtypedata',
            dataType: 'json',
            data: {
                roomType: roomType,
                storeIdx: storeIdx
            },

            success: function(response) {

                console.log("성공 정보얻음 : " + response.roomType);

                var roomPrice = response.roomPrice;
                document.getElementById('roomPrice').value = roomPrice;

            },
            error: function(xhr, status, error) {
                console.error('에러발생data');
            }
        });

    };







    function searchRoomTypeImage(searchDTO){

        $.ajax({
            type: 'GET',
            url: '/roomtypeimage',
            dataType: 'json',
            data: searchDTO,

            success: function(response) {

                const roomTypeDetailImages = response.roomTypeDetailImages;
                const roomTypeMainImage = response.roomTypeMainImage;

                preview.innerHTML = '';
                preview2.innerHTML = '';


                var mainImgName = roomTypeMainImage.imgName;

                var detailimg = document.createElement('div');

                roomTypeDetailImages.forEach(function(data) {
                    detailimg.innerHTML = `<img src="https://gudgh9512.s3.ap-northeast-2.amazonaws.com/static%5c${data.imgName}" width="200" height="100">`;
                    preview.appendChild(detailimg);
                });



                var img = document.createElement('div');
                img.innerHTML = `<img src="https://gudgh9512.s3.ap-northeast-2.amazonaws.com/static%5c${mainImgName}" width="200" height="100">`;


                preview2.appendChild(img);


            },
            error: function(xhr, status, error) {
                console.error('에러발생');
            }
        });

    };


    function updateRoomCheckin(roomCd,roomorderIdx,storeIdx){

        $.ajax({
            type: 'GET',
            url: '/roomcheckin',
            data: {
                roomCd: roomCd,
                roomorderIdx: roomorderIdx,
                storeIdx: storeIdx
            },

            success: function(response) {
                
                console.log("체크인 업데이트 완료")
                location.reload();

            },
            error: function(xhr, status, error) {
                console.error('에러발생');
            }
        });

    };



    function updateRoomPrice(roomPrice,roomType){

        $.ajax({
            type: 'GET',
            url: '/roompriceupdate',
            data: {
                roomPrice: roomPrice,
                roomType: roomType
            },

            success: function(response) {

                console.log("가격 업데이트 완료")
                location.reload();

            },
            error: function(xhr, status, error) {
                console.error('에러발생');
            }
        });

    };


    function roomIdxFind(roomCd,storeIdx,callback){

        $.ajax({
            type: 'GET',
            url: '/roomidxfind',
            data: {
                roomCd: roomCd,
                storeIdx: storeIdx
            },

            success: function(response) {

                callback(response); // 성공 시 콜백 함수 호출

            },
            error: function(xhr, status, error) {
                console.error('에러발생');
            }
        });

    };




    //값 반환
    return {
        selectDistOfStore  : selectDistOfStore,
        selectDistAndBrandOfStore : selectDistAndBrandOfStore,
        registerDistOfStore : registerDistOfStore,
        searchEmptyRoom : searchEmptyRoom,
        searchRoomTypeImage : searchRoomTypeImage,
        updateRoomCheckin : updateRoomCheckin,
        searchRoomTypeData : searchRoomTypeData,
        roomIdxFind : roomIdxFind,
        updateRoomPrice : updateRoomPrice
    };

})();