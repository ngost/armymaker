package com.curonsys.army;

/**
 * Created by ijin-yeong on 2018. 7. 24.. 모델의 크기와 방향값을 정의하는 클래스
 */

class ModelSetData {
    private float scale,rotateX,rotateY,rotateZ;

    ModelSetData(float scale, float rotateX, float rotateY, float rotateZ){
        this.scale = scale;
        this.rotateX = rotateX;
        this.rotateY = rotateY;
        this.rotateZ = rotateZ;
    }

    public float getScale(){
        return this.scale;
    }

    public float getRotateX(){
        return this.rotateX;
    }

    public float getRotateY(){
        return this.rotateY;
    }
    public float getRotateZ(){
        return this.rotateZ;
    }
    public float[] getRotate(){
        float[] result ={this.rotateX,this.rotateY,this.rotateZ};
        return result;
    }

    public void setScale(float scale){this.scale = scale;}
    public void setRotateX(float rotateX){this.rotateX = rotateX;}
    public void setRotateY(float rotateY){this.rotateY = rotateY;}
    public void setRotateZ(float rotateZ){this.rotateZ = rotateZ;}

}
