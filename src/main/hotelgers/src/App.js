import React, { useState } from "react";
import axios from "axios";
import logo from './logo.svg';
import './App.css';

function App() {
    const [data, setData] = useState({});

    function selectData() {
        axios.post('/testData', ["가", "나", "다"])
            .then(function (res) {
                console.log(res.data);
                setData(res.data);
            })
            .catch(function (error) {
                console.error("There was an error fetching the data!", error);
            });
    }

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <div>
                    <button onClick={selectData}>조회</button>
                </div>
                <div>
                    <h2>Data from Server:</h2>
                    <ul>
                        {Object.entries(data).map(([key, value]) => (
                            <li key={key}>{key}: {value}</li>
                        ))}
                    </ul>
                </div>
            </header>
        </div>
    );
}

export default App;