import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js'

import printBalanceScale from "../mesh/scale.js";

const $canvas = document.getElementById('canvas')
document.addEventListener('DOMContentLoaded', () => {
  const addWeightButton = document.getElementById('addWeightButton');
  addWeightButton.addEventListener('click', addWeight);
});

// 장면구조
//1 씬
const scene = new THREE.Scene();
scene.background = new THREE.Color(0x7ccad5)
// scene.background = new THREE.Color(0xFFFFFF)

//2카메라
// const camera = new THREE.PerspectiveCamera(50, window.innerWidth / window.innerHeight, 0.1, 1000);
const camera = new THREE.PerspectiveCamera(50, $canvas.clientWidth / $canvas.clientHeight, 0.1, 1000);
camera.position.set(0, 0, 15);
// camera.lookAt(0,10,0);

//3렌더러
const renderer = new THREE.WebGLRenderer({ canvas: $canvas })
renderer.setSize($canvas.clientWidth, $canvas.clientHeight);
// const renderer = new THREE.WebGLRenderer();
// renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);
renderer.shadowMap.enabled = true

// // AxesHelper 생성 (인자는 축의 길이입니다. 이 값을 조정하여 축의 크기를 결정합니다.)
// const axesHelper = new THREE.AxesHelper(5);
// axesHelper.scale.set(2, 2, 2);
// // 씬에 AxesHelper 추가
// scene.add(axesHelper);

const scale = printBalanceScale();
if (scale) {
  const position = scale.position; // position 속성 접근
  position.y
  console.log(`Scale의 위치: x=${position.x}, y=${position.y}, z=${position.z}`);
} else {
  console.log('Scale 객체를 찾을 수 없습니다.');
}
scale.position.y -= 5;

scene.add(scale);

//책들추가
const modelLoader = new GLTFLoader();
let leftBookModel, rightBookModel;

modelLoader.load('../models/memberBook.glb', (gltf) => {
  leftBookModel = gltf.scene;
  leftBookModel.scale.set(0.2, 0.2, 0.2);
  leftBookModel.rotation.x = Math.PI / 2;
  for (const mesh of leftBookModel.children) {
    mesh.castShadow = true;
  }
});

modelLoader.load('../models/averageBook.glb', (gltf) => {
  rightBookModel = gltf.scene;
  rightBookModel.scale.set(4, 4, 4);
  for (const mesh of rightBookModel.children) {
    mesh.castShadow = true;
  }
})


// 빛
const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
scene.add(ambientLight);

const directionLight = new THREE.DirectionalLight(0xffffff, 1);
directionLight.position.set(-10, 10, 10);
scene.add(directionLight);
directionLight.castShadow = true

const pl1 = new THREE.PointLight(0xff8c00, 1.5);
pl1.position.set(5, 0, 0);
scene.add(pl1);

const pl2 = new THREE.PointLight(0xffe287, 2);
pl2.position.set(-3, 2, 0);
scene.add(pl2);

// OrbitControls
const control = new OrbitControls(camera, renderer.domElement);
const balanceArm = scene.getObjectByName('balanceArm');  // 'balanceArm' 이름으로 객체 찾기
const leftPan = scene.getObjectByName('leftPan');  // 'balanceArm' 이름으로 객체 찾기
const rightPan = scene.getObjectByName('rightPan');  // 'balanceArm' 이름으로 객체 찾기



let leftWeight = 0;
let rightWeight = 0;

function addWeight() {
  const weight = parseFloat(document.getElementById('weight').value);
  const pan = document.getElementById('panSelection').value;

  // 무게에 따라 책의 크기를 조정하는 계수
  const additionalScale = Math.min(Math.max(0.1, weight / 20), 0.5);

  if (pan === 'left' && leftBookModel) {
    const bookClone = leftBookModel.clone();
    const currentScale = 0.2;
    bookClone.position.set(0.3, -0.9, -1.7); // 좌표 조정
    // bookClone.scale.set(currentScale + additionalScale, currentScale + additionalScale, currentScale + additionalScale);
    leftPan.add(bookClone); // 왼쪽 팬의 자식으로 책 추가
    leftWeight += weight;
  } else if (pan === 'right' && rightBookModel) {
    const bookClone = rightBookModel.clone();
    const currentScale = 4;
    bookClone.position.set(-1.2, -0.5, 0); // 좌표 조정
    // bookClone.scale.set(currentScale + additionalScale, currentScale + additionalScale, currentScale + additionalScale);
    rightPan.add(bookClone); // 오른쪽 팬의 자식으로 책 추가
    rightWeight += weight;
  }

  updateBalance(pan, weight);
}


function updateBalance() {
  if (!balanceArm) {
    console.error('Balance arm not found');
    return;  // 객체를 찾지 못하면 여기서 함수 종료
  }
  const angle = (leftWeight - rightWeight) / 100;
  balanceArm.rotation.z = angle;  // 팔만 회전
  console.log(`Balance arm angle updated to: ${angle}`); // 각도 업데이트 로그
}



function animate() {
  control.update();
  renderer.render(scene, camera);
  requestAnimationFrame(animate);
}


animate();
window.addEventListener('resize', () => {
  const width = $canvas.clientWidth;
  const height = $canvas.clientHeight;

  camera.aspect = width / height;
  camera.updateProjectionMatrix();

  renderer.setSize(width, height);
});

// window.addEventListener('resize', () => {
//   camera.aspect = window.innerWidth / window.innerHeight;
//   camera.updateProjectionMatrix();
//   renderer.setSize(window.innerWidth, window.innerHeight);
// })