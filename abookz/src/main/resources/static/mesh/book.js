import * as THREE from 'three';

export default function printBook(color, size) {
  const material = new THREE.MeshPhongMaterial({color: color});
  const bookGeometry = new THREE.BoxGeometry(size.width, size.height, size.depth);
  const book = new THREE.Mesh(bookGeometry, material);
  return book;
}
