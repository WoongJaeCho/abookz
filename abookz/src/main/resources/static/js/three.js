import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import printBalanceScale from "../mesh/scale.js";

const $canvas = document.getElementById('canvas');
const scene = new THREE.Scene();
scene.background = new THREE.Color(0x7ccad5);

const camera = new THREE.PerspectiveCamera(50, $canvas.clientWidth / $canvas.clientHeight, 0.1, 1000);
camera.position.set(0, 0, 20);
// camera.lookAt(0, 20, 0);

const renderer = new THREE.WebGLRenderer({ canvas: $canvas });
renderer.setSize($canvas.clientWidth, $canvas.clientHeight);
renderer.shadowMap.enabled = true;
document.body.appendChild(renderer.domElement);

const scale = printBalanceScale();
scale.position.y = -6;
scene.add(scale);

const modelLoader = new GLTFLoader();
let leftBookModel, rightBookModel;

Promise.all([
  new Promise((resolve, reject) => modelLoader.load('../models/memberBook.glb', gltf => resolve(gltf), undefined, error => reject(error))),
  new Promise((resolve, reject) => modelLoader.load('../models/averageBook.glb', gltf => resolve(gltf), undefined, error => reject(error)))
]).then(([leftGltf, rightGltf]) => {
  leftBookModel = leftGltf.scene;
  rightBookModel = rightGltf.scene;
  setInitialWeights(leftWeight, rightWeight);
}).catch(error => {
  console.error('Model loading failed:', error);
});

const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
scene.add(ambientLight);

const directionLight = new THREE.DirectionalLight(0xffffff, 1);
directionLight.position.set(-10, 10, 10);
directionLight.castShadow = true;
scene.add(directionLight);

const pl1 = new THREE.PointLight(0xff8c00, 1.5);
pl1.position.set(5, 0, 0);
scene.add(pl1);

const pl2 = new THREE.PointLight(0xffe287, 2);
pl2.position.set(-3, 2, 0);
scene.add(pl2);

const control = new OrbitControls(camera, renderer.domElement);
const balanceArm = scene.getObjectByName('balanceArm');
const leftPan = scene.getObjectByName('leftPan');
const rightPan = scene.getObjectByName('rightPan');

let leftWeight = parseFloat(document.getElementById('memberSum').value);
let memberName = document.getElementById('memberName').value;
let rightWeight = parseFloat(document.getElementById('sumAverage').value);

let leftSprite, rightSprite;

document.addEventListener('DOMContentLoaded', () => {
  console.log('Document fully loaded and parsed');
  setInitialWeights(leftWeight, rightWeight);
});

function setInitialWeights(memberWeight, averageWeight) {
  if (!leftBookModel || !rightBookModel) {
    console.error('Model not loaded, cannot set weights');
    return;
  }
  updateTextOnScale();

  const leftScaleAdjustment = Math.min(Math.max(0.01, memberWeight / 50), 0.03);
  const rightScaleAdjustment = Math.min(Math.max(0.1, averageWeight / 50), 0.3);

  const leftBookClone = leftBookModel.clone();
  leftBookClone.scale.set(0.2 + leftScaleAdjustment, 0.2 + leftScaleAdjustment, 0.2 + leftScaleAdjustment);
  leftBookClone.rotation.x = Math.PI / 2;
  leftBookClone.position.set(0.3, -0.9, -1.7);
  leftPan.add(leftBookClone);
  leftWeight = memberWeight;

  const rightBookClone = rightBookModel.clone();
  rightBookClone.scale.set(4 + rightScaleAdjustment, 4 + rightScaleAdjustment, 4 + rightScaleAdjustment);
  rightBookClone.position.set(-1.2, -0.5, 0);
  rightPan.add(rightBookClone);
  rightWeight = averageWeight;

  updateBalance();
}

function updateBalance() {
  console.log('updateBalance function called');
  if (!balanceArm) {
    console.error('Balance arm not found');
    return;
  }
  const angle = (leftWeight - rightWeight) / 50;
  balanceArm.rotation.z = angle;
  updateTextOnScale();  // 새로운 무게로 텍스트 업데이트
  console.log(`Balance arm angle updated to: ${angle}`);
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

function createTextTexture(text, fontSize = 32, textColor = '#FFF', bgColor = '#000') {
  const canvas = document.createElement('canvas');
  const context = canvas.getContext('2d');

  canvas.width = 350;
  canvas.height = 128;
  context.fillStyle = bgColor;
  context.fillRect(0, 0, canvas.width, canvas.height);

  context.font = `${fontSize}px Arial`;
  context.fillStyle = textColor;
  context.textAlign = 'center';
  context.textBaseline = 'middle';
  context.fillText(text, canvas.width / 2, canvas.height / 2);

  const texture = new THREE.Texture(canvas);
  texture.needsUpdate = true;

  return texture;
}

function createTextSprite(text) {
  const texture = createTextTexture(text);
  const material = new THREE.SpriteMaterial({ map: texture });
  const sprite = new THREE.Sprite(material);
  sprite.scale.set(5, 2.5, 1);  // 적당한 크기 조절
  return sprite;
}

function updateTextOnScale() {
  const leftText = `${memberName}평균 : ${leftWeight.toFixed(2)}Kg`;
  const rightText = `전체회원평균 : ${rightWeight.toFixed(2)}Kg`;

  if (!leftSprite) {
    leftSprite = createTextSprite(leftText);
    leftSprite.position.set(-4, 4, 0);
    scene.add(leftSprite);
  } else {
    leftSprite.material.map = createTextTexture(leftText);
    leftSprite.material.map.needsUpdate = true;
  }

  if (!rightSprite) {
    rightSprite = createTextSprite(rightText);
    rightSprite.position.set(4, 4, 0);
    scene.add(rightSprite);
  } else {
    rightSprite.material.map = createTextTexture(rightText);
    rightSprite.material.map.needsUpdate = true;
  }
}
