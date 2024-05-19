import React, { useEffect, useState } from 'react';
import axios from 'axios';

function ReactComponent() {
    const [memberDTO, setMemberDTO] = useState(null);

    useEffect(() => {
        // 백엔드에서 데이터 가져오기
        axios.get('/react')
            .then(response => {
                // 데이터 받아오기 성공 시 상태에 저장
                setMemberDTO(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, []);

    return (
        <div>
            {/* memberDTO가 존재하는 경우에만 화면에 표시 */}
            {memberDTO && (
                <div>
                    <h1>{memberDTO.name}</h1>
                    <p>{memberDTO.email}</p>
                    {/* 기타 데이터 필드들을 표시 */}
                </div>
            )}
        </div>
    );
}

export default ReactComponent;