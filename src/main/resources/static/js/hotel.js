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

                var emptyRoomResultTable = $('#emptyRoomResultTable');

                emptyRoomResultTable.empty(); // 기존 결과 제거

                // 새로운 행 추가
                response.forEach(function(item) {

                    var newRow = `
                        <tr>
                            <td>${item.roomType}</td>
                            <td>
                                <img src="https://gudgh9512.s3.ap-northeast-2.amazonaws.com/static%5c${item.roomMainimgName}" width="400" height="200">
                            </td>
                            <td>
                                <button class="btn button reserve-btn" type="button" data-room-idx="${item.roomIdx}" data-room-type="${item.roomType}">예약하기</button>
                            </td>
                        </tr>`;
                    emptyRoomResultTable.append(newRow);
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



    //값 반환
    return {
        selectDistOfStore  : selectDistOfStore,
        selectDistAndBrandOfStore : selectDistAndBrandOfStore,
        registerDistOfStore : registerDistOfStore,
        searchEmptyRoom : searchEmptyRoom
    };

})();