import * as THREE from 'three';

export default function printCustomBalanceScale() {
  const scale = new THREE.Group();

  const Material = new THREE.MeshStandardMaterial({
    color: 0xaaaaaa,
    metalness: 0.8,
    roughness: 0.3
  });

  // 팔 생성 (이미지에 맞게 조정)
  const armGeometry = new THREE.BoxGeometry(10, 0.2, 1);
  const arm = new THREE.Mesh(armGeometry, Material);
  arm.name = 'balanceArm';  // 팔 객체에 이름 부여
  arm.position.y = 8; // 팔의 높이 조정
  scale.add(arm);

  // 팔의 두 끝에 접시 생성
  const panGeometry = new THREE.ConeGeometry(3, 1, 32); // 접시의 바닥 지름, 높이, 세그먼트 수

  const leftPan = new THREE.Mesh(panGeometry, Material);
  leftPan.name = 'leftPan';
  const rightPan = new THREE.Mesh(panGeometry, Material);
  rightPan.name = 'rightPan';
  arm.add(leftPan);
  arm.add(rightPan);
  // scale.add(leftPan);
  // scale.add(rightPan);


// 접시의 위치를 팔의 끝에 맞추어 조정
  leftPan.position.set(-4.7, -3, 0);
  leftPan.rotation.x = -Math.PI ; // 접시가 위를 향하도록 회전

  rightPan.position.set(4.7, -3, 0);
  rightPan.rotation.x = -Math.PI  ; // 접시가 위를 향하도록 회전

  // 기둥 생성 (이미지에 맞게 길이 조정 및 받침대 추가)
  const postHeight = 10; // 기둥의 높이
  const postGeometry = new THREE.CylinderGeometry(0.5, 0.5, postHeight, 12);
  const post = new THREE.Mesh(postGeometry, Material);
  post.position.y = postHeight / 2; // 기둥의 중심이 Y축의 0에 오도록 조정
  scale.add(post);

  // 받침대 생성
  const baseGeometry = new THREE.BoxGeometry(5, 0.5, 3);
  const base2Geometry = new THREE.BoxGeometry(8, 0.5, 3);
  const base = new THREE.Mesh(baseGeometry, Material);
  const base2 = new THREE.Mesh(base2Geometry, Material);
  base.position.y = 0.25; // 받침대의 두께의 절반
  base2.position.y = -0.25; // 받침대의 두께의 절반
  scale.add(base);
  scale.add(base2);


  // 체인 생성 및 접시와 연결 (체인 모델링 과정 필요)
// 체인 고리 생성
  const chainGeometry = new THREE.TorusGeometry(0.1, 0.05, 8, 16);


  // 체인 생성 함수
  function createChain(x, y, z, numLinks) {
    let chainGroup = new THREE.Group();

    for (let i = 0; i < numLinks; i++) {
      let link = new THREE.Mesh(chainGeometry, Material);
      link.position.x = x;
      link.position.y = y - (i * 0.2);  // 고리 사이의 거리를 설정
      link.position.z = z;

      // 고리를 회전하여 체인처럼 보이게 함
      if (i % 2 === 0) {
        link.rotation.y = Math.PI / 2;
      }

      chainGroup.add(link);
    }

    return chainGroup;
  }

  // 왼쪽과 오른쪽 체인 생성
  const leftChain = createChain(-4.5, -0.3, 0, 17);
  leftChain.rotation.x = THREE.MathUtils.degToRad(-47);
  const leftChain2 = createChain(-4.5, -0.3, 0, 17);
  leftChain2.rotation.x = THREE.MathUtils.degToRad(47);
  const rightChain = createChain(4.5, -0.3, 0, 17);
  rightChain.rotation.x = THREE.MathUtils.degToRad(-47);
  const rightChain2 = createChain(4.5, -0.3, 0, 17);
  rightChain2.rotation.x = THREE.MathUtils.degToRad(47);

  // 체인을 팔과 접시에 연결
  arm.add(leftChain);
  arm.add(leftChain2);
  arm.add(rightChain);
  arm.add(rightChain2);


  // 저울 반환
  scale.name = 'balanceScale';
  return scale;
}
