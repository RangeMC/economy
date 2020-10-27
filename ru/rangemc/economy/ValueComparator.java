package ru.rangemc.economy;

import java.util.Comparator;
import java.util.Map;

class ValueComparator implements Comparator {
  Map map;
  
  public ValueComparator(Map base) {
    this.map = base;
  }
  
  public int compare(Object a, Object b) {
    return (((Integer)this.map.get(a)).intValue() >= ((Integer)this.map.get(b)).intValue()) ? -1 : 1;
  }
}
