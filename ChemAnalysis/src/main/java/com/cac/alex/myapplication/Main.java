package com.cac.alex.myapplication;

/**
 * Author: Ben Grass
 * Utility class that computes quantumNumbers for a given element(string) and a given charge(int)
 */

class Main {

  public static String quantumNumbers(String elementAbbr, int charge){
    String electronConfig = "";
    int atomicNum = PeriodicTable.periodicTable.get(elementAbbr) + charge;
    int level = 1;
    String subshell;
    boolean removeFromS = false;
    while(atomicNum > 0){
      //s
      subshell = "s";
      if(atomicNum-2 >= 0){
        electronConfig += level + subshell + 2 + " ";
        atomicNum -= 2;
      }
      else{
        electronConfig += level + subshell + 1;
        atomicNum -= 1;
        break;
      }
      //f
      if(level >= 6 && atomicNum != 0){
        subshell = "f";
        if(atomicNum-14 >= 0){
          electronConfig += (level-2) + subshell + 14 + " ";
          atomicNum -= 14;
        }
        else{
          electronConfig += (level-2) + subshell + atomicNum;
          atomicNum -= atomicNum;
          break;
        }
      }
      //d
      if(level >= 4 && atomicNum != 0){
        subshell = "d";
        if(atomicNum-10 >= 0){
          electronConfig += (level-1) + subshell + 10 + " ";
          atomicNum -= 10;
        }
        else{
          if(atomicNum != 5){
            electronConfig += (level-1) + subshell + (atomicNum+1);
            removeFromS = true;
          }
          else{
            electronConfig += (level-1) + subshell + (atomicNum);
          }
          atomicNum -= atomicNum;
          break;
        }
      }
      //p 
      if(level != 1 && atomicNum != 0){
        subshell = "p";
        if(atomicNum-6 >= 0){
          electronConfig += level + subshell + 6 + " ";
          atomicNum -= 6;
        }
        else{
          electronConfig += level + subshell + atomicNum;
          atomicNum -= atomicNum;
          break;
        }
      }
      level++;
    }
    if(removeFromS){
      for(int i=(electronConfig.length() - 1); i >= 0; i--){
        String charAt = electronConfig.substring(i,(i+1));
        if(charAt.equals("s")){
          electronConfig = electronConfig.substring(0, (i+1)) + "1" + electronConfig.substring(i+2);
          break;
        }
      }
    }
    return electronConfig;
  }
}