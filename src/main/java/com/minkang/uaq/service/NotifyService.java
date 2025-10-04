package com.minkang.uaq.service;

import com.minkang.uaq.UAQPlugin;
import java.util.*;

public class NotifyService {
    protected final UAQPlugin plugin;
    public NotifyService(UAQPlugin plugin){ this.plugin = plugin; }
    public java.util.List<String> list(java.util.UUID u){ return new ArrayList<>(); }
}
